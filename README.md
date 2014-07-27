[Different Models](#different-models)
| [Options](#options)
| [Operands](#operands)
| [Help Facilities](#help-facilities)
| [Command Line](#command-line-syntax)
| [Usage Text](#usage-text-syntax)
| [Operating Variables](#operating-variables)

ArgFace
=======
Have you ever seen this kind of message from a computer program?

    Usage:
        program [-a] [-b|--brand] [-c <name>] [find <pattern>] <file>...
    Options:
        -a                 Process all files as a unit.
        -b, --brand		   Brand each line.
        -c          <name> Prefix name.

Of course!
It's the "usage" text that is printed when you ask for `--help`.
Most people *do* find that this message is helpful.
So why not design a command line interface that understands this specification?

That's exactly what **ArgFace** does.
It creates the command line interface from a program's "usage" text.
Then it parses the command line to yield values for corresponding options and arguments.

Here's a brief example:

    public class Program {
    	private final String usageText =
    	  "Usage: program [-a] [-b/--brand] [-c <name>] [find <pattern>] <file>...";
    	private boolean aOption;
    	private boolean bOption;
    	private boolean cOption;
    	private String  cName;
    	private boolean findOperand;
    	private String  patternOperand;
    	private String [] fileOperand;
    	
		public static void main (String [] args) {
      		Program prog = new Program();
      		ArgFace argFace = ArgPrototype.create(usageText, prog);
      		if (argFace == null) {
      			System.exit(1);
      		}
      		int nArg = argFace.parse(args);
      		if (nArg < 0) {
      			System.exit(1);
      		}
      		if (prog.aOption) {
      			System.out.println("-a option specified");
			...

This example creates an ArgFace object using the ArgPrototype model.
Then the command line arguments are parsed.
Finally, the boolean class member variable `aOption` is used
to navigate through the program.

The ArgPrototype model uses reflection to access private member variables.
Another model provides standard access through public getter and setter methods.
There is also a procedural model that uses no reflection at all.
See: [Different Models](#different-models)

The example shows private member variables that represent the command line options
and arguments that use names determined from the usage text.
The "-a" option requires a `boolean` variable with the name "aOption".
If the option includes an alternative, as with the "-b/--brand" specification,
the variable can be named either "bOption" or "brandOption".
See: [Options](#options)

The naming convention uses lower camel case throughout.
Thus, an option "--dir-path" would use the variable declaration:

	private boolean dirPathOption;

Anything in the usage text that is not an option is an operand.
Operands come in two flavors: literals and variables.
The literal operand "find" is followed by a variable operand "&lt;pattern&gt;".
The "&lt;file&gt;" specifier indicates a variable operand.
See: [Operands](#operands)

All literal operands are declared as `boolean` variables.
Variable operands are declared as `String` variables.
Because the "&lt;file&gt;" operand is followed by the ellipsis "...", it is
a repeating item and must be declared as an array or a list:

	private String [] fileOperand;
	// --- or ---
	private List<String> fileOperand;

By default, option names end with the **option suffix** "Option" and operands end
with the **operand suffix** "Operand".
It is possible to change these defaults.
See: [Operating Variables](#operating-variables)

### Different Models

ArgFace includes various operational models that determine the way that information is passed between the interface and the program. These include:

* **ArgPrototype** - uses reflection to access fields defined by the program.
The interface does not require getter and setter methods for this model but will
instead attempt to access even private fields of the program. Note, however, that
private access will fail if there is a *SecurityManager* in place for the program.

* **ArgStandard** - also uses reflection to access fields. But unlike *ArgPrototype*,
it uses only public getter and setter methods to exchange information. This model may
be used even when the program is run with a *SecurityManager*.

* **ArgProcedure** - does not use any reflection. In this model, information is exchanged
by making method calls to the interface. Although these methods are available to all
of the models, they are not always necessary.

The creation phase of ArgFace establishes which model will be used.
This is performed with code of the form:

    ArgFace argFace = <Model>.create(usageText, object);
    
### Options

ArgFace supports both short, one-letter options and the longer GNU style options.
Short options begin with a single dash (hyphen). Long options begin with two dashes.

     -a    (short option)
     --all (long option)

Short options may be specified in a letter group.

     -abc (same as) -a -b -c

Each option may be defined with two variants. Usually there is a short option and a
long option. But two long options may also be used.

     -a/--all          (two versions of the same option)
     --all/--all-files (also valid)

Both upper and lower case letters may be used with any option.

     -abcABC           (both upper and lower case in letter group)
     -p/--Page         (long option in title case)
     --DIR <PATH>      (all capitals)

ArgFace expects short option member variable names to be constructed from the option
letter followed by the option suffix (default is "Option").

     -a => aOption
     -A => AOption

The getter and setter names for short options also use the option letter.

     -a => setaOption, getaOption
     -A => setAOption, getAOption

Long options use camel case for member variable names. Capitalized option names
remain capitalized.

     --dir => dirOption
     --base-dir => baseDirOption
     --DIR => DIROption

The getters and setters always capitalize the first letter of long options.
This means that you must not mix both title case and lower case for the same name.

     --dir => setDirOption, getDirOption
     --base-dir => setBaseDirOption, getBaseDirOption
     --Dir => setDirOption, getDirOption (title case same as --dir above)
     --DIR => setDIROption, getDIROption

Options may take an argument and that argument may be specified as optional.

     --base-dir <path>  (option with an argument)
     --base-dir [path]  (option with optional argument)

If an option has two versions and an argument, the argument applies to both names.

     --dir/--base-dir <path> (the path applies to both versions)

The naming convention for option arguments is similar to what has already been mentioned.
Member variable names are constructed by first using the option name followed by the
argument name.

     --dir <path> => dirPath (camel case capitalizes argument name)
     --base-dir [path] => baseDirPath (capitalize words after dash separator)
     --Dir <path> => DirPath (capitals are preserved)
     --DIR [PATH] => DIRPATH

For the getters and setters of option arguments, again the first letter of long option
names are capitalized. Do not mix title case and lower case for option names with the
same spelling.

     --dir <path> => setDirPath, getDirPath
     --base-dir [path] => setBaseDirPath, getBaseDirPath
     --Dir <path> => setDirPath, getDirPath (title case same as --dir above)
     --DIR [PATH] => setDIRPATH, getDIRPATH (capitals are preserved)

In order to specify that an optional argument is included on the command line, and to differentiate the argument from other operands, an equal sign (=) or colon (:) is used
to tie the option to the argument.

    prog [--dir/--base-dir [path]] <file>...  (usage text)
    $ prog --dir /directory/path file1 file2  (/directory/path treated as first file)
    $ prog --dir=/directory/path file1 file2  (optional argument indicated by equal sign)

For short options with an optional argument, the equal sign or colon will work just as
well. However, short options may also indicate an argument by entering it adjacent to the
option letter with no intervening spaces.

    prog [-d/--dir [path]] <file>...  (usage text)
    $ prog -d/directory/path file1    (adjacent to option letter)
    $ prog -d=/directory/path file1   (also valid)

The equal sign or colon indicators may include intervening spaces as long as the proper
order is maintained.

    $ prog -d = /directory/path file1  (spaces are allowed)
    $ prog -d =/directory/path file1   (at start of argument)
    $ prog -d: /directory/path file1   (also valid)

Lastly, options don't have to be optional. But this only makes sense in terms of
multiple usage specifications with the option indicating which one applies.

    Usage: prog -s [-a] <file>...
           prog -t <path>

### Operands

Anything on the command line that is not an option is an operand.

### Help Facilities

ArgFace includes a set of help facilities that respond to the following
user input options:

* **--help (-h)**
Displays a help message and exits. This includes the usage text and options section.
This can be followed by an optional "helpText" message that the program may
include to supply some additional information.
The short option "-h" will be assigned as an alternative to this option as long as
it is not already in use elsewhere.

* **--version (-v)**
Shows the program version number information.
This is supplied by the program as the "versionText".
If the short option "-v" has not already been assigned elsewhere, it may be used
as an alternative.

* **--about (-a)**
Displays an about message for the program.
Another optional feature to supply information about the program.
This might include the version number, a realease date, an email address,
company name, etc.
The program supplies this as the "aboutText".
The short option "-a" may also be used for this option as long as it is not
already in use elsewhere.

These options are available as long as the proper "text" information is supplied
by the program.
They will appear in a separate usage specification of the help output.
If the usage text contains any of these options, they will appear as specified by
the program.

A program may choose to handle these options in another way or not at all.
This is made possible by setting the operating variable "suppressHelp" to true.
Add the options in the usage text and define member variables in the usual manner.

    private final String usageText = "program --help | --version";
    private boolean helpOption;
    private boolean versionOption;



### Command Line Syntax

This section discusses what the user can enter on the command line to run a
program and how optional material is presented to specify the desired
outcome. Here are some references that demonstrate several variations in how
the command line syntax has developed.

[The Open Group (POSIX) - Utility Argument Syntax](
http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html)<br>
[Program Argument Syntax Conventions](
http://www.gnu.org/software/libc/manual/html_node/Argument-Syntax.html)<br>
[Standards for Command Line Interfaces](
http://www.gnu.org/prep/standards/html_node/Command_002dLine-Interfaces.html)

Because of the historical significance of a number of Unix utilities, the
Posix standards are a serious concern for this effort. Therefore, option
letters or short options are given primary support.

The GNU form of long options is also available. The only conflict here is
that a command line option may be specified with a single hyphen even for the
long names and that this might be unintentionally mistaken for a group of
letter options.

    [-abcde] (letter options)
    [--ace]  (long option)
    
    $ program -ace  (ambiguous)
    $ program --ace (not ambiguous)
    
This implementation will detect this situation and treat a single dash long
option as an error if there is a corresponding letter group of the same
configuration.

The Posix standard outlines the command line format as follows:
 
    $ proram_name [options] [operands...]
    
This form requires that all options come before any non-option arguments. The
alternative mixes options and operands in any order on the command line.

    $ program_name [option | operand]...
    
This implementation provides support for both varieties. The usage text
should make it clear when options are expected to precede the operands.

To enforce the POSIX version of the command line format (options before operands),
a program may define the operating variable "posixFormat" to be true.
TODO: need a link to the posix sample code.

As was already mentioned, options may be specified by the user on the command
line with both a single dash or a double dash no matter whether the option is
a short option or a long option. However, a short
option letter group will only be recognized following a single dash.

    $ program -a --option file.txt
    $ program --a -option file.txt # is also valid
    $ program -abc # short form letter group

If an option takes an argument, then the argument should follow the option as
the next token on the command line. For a short option, the argument may be
placed adjacent to the option letter in the same token. For any option, the
argument may follow the option with an intervening separation character. This
implementation supports the use of both the "=" and ":" as the separation
character.

    $ program -x Argument file.txt
    $ program -xArgument file.txt
    $ program -x:Argument file.txt
    $ program -option Argument file.txt
    $ program -option=Argument file.txt
    $ program -option: Argument file.txt
    $ program -option : Argument file.txt

If an option argument is optional, there are a couple of ways to indicate
that the argument is actually being specified. First, for short options, you
can place the argument adjacent to the letter option in the same token
(-xArgument). Or you can use one of the separation characters "=" or ":"
after the option letter (-x=Argument). Long options require the use of the
separation character. In addition, when the separation character is used, it
may be specified in the next token of the command line, even by itself and
then followed by the argument.

    $ program -rArgument file.txt
    $ program -r=Argument file.txt
    $ program -r = Argument file.txt
    $ program -option = Argument file.txt
    $ program -option =Argument file.txt
    $ program -option: Argument file.txt

### Usage Text Syntax

Other command line interfaces (CLI) require a long sequence of procedural code
just to get the options and arguments to make sense.
ArgFace does away with all that clutter.
Rather than a load of initialization code, the usage text fully describes what is
expected by the program.

The overall format of the usage text is:

    [usage[:]] program-name <usage-specification>
        [program-name <usage-specification>] ...
    [options[:]
        <option-specification> ... ]

The "usage" and colon are optional.
The program name marks the start of each usage specifier.
Multiple usage specifiers are mutually exclusive.

The "options" section is used to define, clarify, or extend the program options. For example:

    usage: prog [-a] define <variable-name> [<value>]
    options:
        -a, --allow Grant redefinition privilege.
        
Here the "-a" option specified in the usage section is paired with an
alternate longer name "--allow" and assigned a help message.
An equivalent specification would be:

    usage: prog [-a/--allow] 'Grant redefinition privilege.'
        define <variable-name> [<value>]

These are the special character rules for a usage specification:

* Single dash precedes a short, one-letter option name. `-a`
* Double dash precedes a long, option name. If the name uses
multiple words, the words should be joined by a single dash.
`--word-option-name`
* Square brackets denote optional material. `[ optional ]`
* Angle brackets delimit a named entity. This may
include option arguments and variable operands. The name inside the
brackets is part of a corresponding member variable name in the program.
`< variable >`
* Items inside parenthesis indicate a mutually exclusive choice.
These items are separated by the vertical bar or pipe character.
`( one | two )`
* Single quotes surrounding text following an option specification is
used as the help text for an option. `[-a] 'Help text'`

### Operating Variables

The following `boolean` variables define variations in the operation of ArgFace.
If they are not defined, standard behavior will occur.

* **allowOverwrite** - allows option arguments specified more than once to overwrite
a previous value. Standard behavior is to treat this situation as a user error. If
this variable is defined as `false`, overwrites will not be allowed resulting in the
persistence of the first argument value.
* **suppressHelp** - suppress use of the help facilities. This results in no output
when the `--help` `--version` or `--about` options are specified by the user. It is
up to the program to detect the presence of these options and to supply an appropriate
response.
* **posixFormat** - enables the posix format operating mode.
* **sortOptions** - enables the sorting of options in the help output.

These `String` variables are used to override their default values.

* **optionSuffix** - defines the option suffix used to construct variable names.
The default value is "Option".
* **operandSuffix** - defines the operand suffix used to construct variable names.
The default value is "Operand".



