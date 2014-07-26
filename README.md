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
long option. But two long options is also permitted.

    -a/--all          (two versions of the same option)
    --all/--all-files (also valid)

### Operands

Anything on the command line that is not an option is an operand.

### Help Facilities

ArgFace includes a set of help facilities that respond to the following
user input options:

* **--help**
Displays a help message and exits. This includes the usage text and options section.
This can be followed by an optional "helpText" message that the program may
include to supply some additional information.
The short option "-h" will be assigned as an alternative to this option as long as
it is not already in use elsewhere.

* **--version**
Shows the program version number information.
This is supplied by the program as the "versionText".
If the short option "-v" has not already been assigned elsewhere, it may be used
as an alternative.

* **--about**
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



