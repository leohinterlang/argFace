/**
 *+
 *  ArgFaceInfo.java
 *	1.0.0	Apr 21, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * <h3>Don't just write your usage text, parse it.</h3>
 *
 * <b>ArgFace</b> (a contraction of argument and interface), is a command line
 * interface designed to get projects up and running quickly using different
 * models for different stages of the development cycle. There are a lot of Java
 * command line interfaces available. The problem is that they are mostly
 * procedural in nature. They build the command line syntax by a series of
 * method calls to define various options and arguments in a long sequence of
 * code.
 * <p>
 * The {@code ArgFace} command line interface was built from the ground up to be
 * specification centric. Rather than a lot of procedural code, it parses the
 * "usage" text as the definition of the command line syntax. Many of us have
 * created the "usage" text for the purpose of explaining a program's external
 * interface to the user. Now, it becomes more than what you print when they
 * enter --help.
 * <p>
 * Here is a typical example of how {@code ArgFace} can be used.
 * 
 * <pre>
 *  public static void main (String[] args) {
 *      ProgramClass thisClass = new ProgramClass();
 *      ArgFace argFace = ArgPrototype.create(usageText, thisClass);
 *      int nArg = argFace.parse(args);
 *      if (nArg < 0) {
 *          System.exit(1);
 *      }
 *      while (nArg < args.length) {
 *          String arg = args[nArg++];
 * </pre>
 * <p>
 * That's all there is to it. Well, almost. You must also supply the "usage"
 * text that formally describes the command line options and arguments.
 * Something like this:
 * 
 * <pre>
 *  private final String usageText = "program [-a|--all] [-b|--base-dir[&lt;path>]]\n" +
 *      "[-c &lt;name>]... [--help] [--version] &lt;operands>...";
 * </pre>
 * You don't even have to set the "usage" text as a parameter. The "prototype"
 * model of {@code ArgFace} will find it using reflection. There are three
 * different models to choose from. There is the "standard" model that uses
 * reflection only for public access to getter and setter methods to access
 * program variables. And there is a "procedural" model that uses no reflection
 * at all.
 * <p>
 * For now, we will focus on the {@code ArgPrototype} model until we work out
 * the details of our interface.
 * <p>
 * The next thing that we need is to supply the variables that match up with the
 * names of the options and operands of our specification; the "usage" text. For
 * an option, we can declare a boolean variable using standard naming
 * conventions.
 * <p>
 * For example, the -a option and its alternative --all must have a
 * corresponding variable named either "aOption" or "allOption". Either one will
 * work just as well as the other. For -b and --base-dir, the variable should be
 * either "bOption" or "baseDirOption"; whichever you prefer.
 * <p>
 * When there is no alternate form as with the -c option, use the variable name
 * "cOption".
 * <p>
 * For options that require an argument, like {@code [-c <name>]}, there must be
 * a variable for the option argument &lt;name>. In this case, the argument
 * variable will be specified as "cName". In the case of {@code
 * [-b|--base-dir[<path>]]}, with the alternative form, the variable name should
 * be either "bPath" or "baseDirPath".
 * <p>
 * The type of the argument variable depends on whether or not the option or the
 * option argument is repeatable. This is indicated by the elipses "..." in the
 * usage notation. Since the -c option is repeatable, "cName" must be an array
 * or a list. Just make the declaration and {@code ArgFace} will do the rest.
 * <p>
 * Including the option variables with our "usage" text, we now have:
 * 
 * <pre>
 *  public class MyProgram {
 *      private final String versionText = "MyProgram 0.1.4";
 *      private final String usageText = "MyProgram [-a|--all] [-b|--base-dir[&lt;path>]]" +
 *          "[-c &lt;name>]... [-h|--help] [--version] &lt;operands>...";
 *      private boolean     aOption;
 *      private boolean     baseDirOption;
 *      private String      baseDirPath;
 *      private boolean     cOption;
 *      private String []   cName;
 *      private boolean     helpOption;
 *      private boolean     versionOption;
 *      
 *      public static void main (String [] args) {
 *          MyProgram program = new MyProgram();
 *          ArgFace argFace = ArgPrototype.create();
 *          int nArg = argFace.parse(program, args);
 *          if (nArg < 0) {
 *              System.exit(1);
 *          }
 *          if (program.versionOption) {
 *              System.out.println("MyProgram version: " + version);
 *          }
 *          while (nArg < args.length) {
 *              // process operands     
 *  </pre>
 * That's it! Using the "prototype" model, you don't even need setter methods
 * for the variables.
 * <h2>Command Line Syntax</h2>
 * This section discusses what the user can enter on the command line to run a
 * program and how optional material is presented to specify the desired
 * outcome. Here are some references that demonstrate several variations in how
 * the command line syntax has developed.
 * <p>
 * <a href=
 * "http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html">
 * The Open Group (POSIX) - Utility Argument Syntax</a><br>
 * <a href=
 * "http://www.gnu.org/software/libc/manual/html_node/Argument-Syntax.html"> GNU
 * Program Argument Syntax Conventions</a><br>
 * <a href=
 * "http://www.gnu.org/prep/standards/html_node/Command_002dLine-Interfaces.html"
 * > Standards for Command Line Interfaces</a><br>
 * <p>
 * Because of the historical significance of a number of Unix utilities, the
 * Posix standards are a serious concern for this effort. Therefore, option
 * letters or short options are given primary support.
 * <p>
 * The GNU form of long options is also available. The only conflict here is
 * that a command line option may be specified with a single hyphen even for the
 * long names and that this might be unintentionally mistaken for a group of
 * letter options.
 * 
 * <pre>
 *  [-abcde] letter options
 *  [--ace] long option
 *  $ program -ace is ambiguous but --ace is not.
 * </pre>
 * This implementation will detect this situation and treat a single dash long
 * option as an error if there is a corresponding letter group of the same
 * configuration.
 * <p>
 * The Posix standard outlines the command line format as follows:
 * 
 * <pre>
 *  $ proram_name [options] [operands...]
 * </pre>
 * This form requires that all options come before any non-option arguments. The
 * alternative mixes options and operands in any order on the command line.
 * 
 * <pre>
 *  $ program_name [option | operand]...
 * </pre>
 * This implementation provides support for both varieties. The usage text
 * should make it clear when options are expected to precede the operands.
 * <p>
 * As was already mentioned, options may be specified by the user on the command
 * line with both a single dash or a double dash no matter whether the option is
 * a short form letter option or a long form word option. However, a short
 * option letter group will only be recognized following a single dash.
 * 
 * <pre>
 *  $ program -a --option file.txt
 *  $ program --a -option file.txt # is also valid
 *  $ program -abc # short form letter group
 * </pre>
 * If an option takes an argument, then the argument should follow the option as
 * the next token on the command line. For a short option, the argument may be
 * placed adjacent to the option letter in the same token. For any option, the
 * argument may follow the option with an intervening separation character. This
 * implementation supports the use of both the "=" and ":" as the separation
 * character.
 * 
 * <pre>
 *  $ program -x Argument file.txt
 *  $ program -xArgument file.txt
 *  $ program -x:Argument file.txt
 *  $ program -option Argument file.txt
 *  $ program -option=Argument file.txt
 *  $ program -option: Argument file.txt
 *  $ program -option : Argument file.txt
 * </pre>
 * If an option argument is optional, there are a couple of ways to indicate
 * that the argument is actually being specified. First, for short options, you
 * can place the argument adjacent to the letter option in the same token
 * (-xArgument). Or you can use one of the separation characters "=" or ":"
 * after the option letter (-x=Argument). Long options require the use of the
 * separation character. In addition, when the separation character is used, it
 * may be specified in the next token of the command line, even by itself and
 * then followed by the argument.
 * 
 * <pre>
 *  $ program -rArgument file.txt
 *  $ program -r=Argument file.txt
 *  $ program -r = Argument file.txt
 *  $ program -option = Argument file.txt
 *  $ program -option =Argument file.txt
 *  $ program -option: Argument file.txt
 * </pre>
 * 
 * <h2>Usage Syntax</h2>
 * The format of the "usage" text is as follows:
 * 
 * <pre>
 *  &lt;usage-specification&gt; ::= "usage" [:] &lt;usage-spec&gt; [&lt;usage-spec&gt;...]
 *      ["Options" [:] &lt;options-spec&gt;]
 *  
 *  &lt;usage-spec&gt; ::= &lt;program-name&gt; [&lt;option-spec&gt; | &lt;operand-spec&gt;]
 *  
 *  &lt;options-spec&gt; ::= &lt;short-option&gt; [, &lt;long-option&gt;] [option-description]
 *  
 *  &lt;option-spec&gt; ::= &lt;short-option&gt; | &lt;long-option&gt; | &lt;letter-group&gt; | &lt;option-pair&gt;
 *  
 *  &lt;operand-spec&gt; ::= &lt;operand-variable&gt; | &lt;operand-literal&gt;
 *      
 *  &lt;short-option&gt; ::= -&lt;short-name&gt; [&lt;option-arg&gt;]
 *  
 *  &lt;long-option&gt; ::= --&lt;long-name&gt; [&lt;option-arg&gt;]
 *  
 *  &lt;letter-group&gt; ::= -&lt;short-name&gt; &lt;short-name&gt;... [&lt;option-arg&gt;]
 *  
 *  &lt;option-pair&gt; ::= -&lt;short-name&gt; | --&lt;long-name&gt; '|' --&lt;long-name&gt; [&lt;option-arg&gt;]
 *  
 *  &lt;operand-variable&gt; ::= '&lt;' operand-name '&gt;'
 *  
 *  &lt;operand-literal&gt; ::= &lt;text&gt;
 *  
 *  &lt;short-name&gt; ::= &lt;letter-or-digit&gt;
 *  
 *  &lt;long-name&gt; ::= &lt;letter-or-digit&gt; [&lt;letter-or-digit&gt | '-' | '_' ]...
 *      [&lt;letter-or-digit&gt;]
 * </pre>
 * 
 * <ul>
 * <li>The usage text may be a single String or a String array declared as:
 * 
 * <pre>
 *  private String usage = "Usage: ProgramName ...";
 *  -OR-
 *  private String [] usage = { "Usage: ProgramName ...", "[--option] ...", ... }; 
 * </pre>
 * For the String array, the strings are simply joined together to make up the
 * completed text. A newline character is added at the end of each string to
 * ensure that there is a break between tokens on adjacent lines.</li>
 * <p>
 * <li>The usage text begins with the word "usage" (any case) and is followed by
 * an optional colon ":" and a space. Next is the name of your program.</li>
 * <p>
 * <li>The usage specification allows multiple versions of a program invocation
 * to appear in the usage text by repeating the program name as in:
 * 
 * <pre>
 *  private String [] usage = {
 *      "Usage:",
 *      "MyProgram -a &lt;file&gt;...",
 *      "MyProgram -h | --help",
 *      "Myprogram --version"
 *  };
 * </pre>
 * Note that these different invocations are mutually exclusive in terms of
 * processing modality.</li>
 * <p>
 * <li>Following the name of the program in the usage specification you can
 * provide options and operands. Operands may be substitution variables or
 * literal values. For example:
 * 
 * <pre>
 *  private String usage = "Usage: MyProgram [-a] find &lt;pattern&gt; &lt;file&gt;";
 * </pre>
 * In this example, the option -a is shown between square brackets to show that
 * it is not required to be included on the command line. The word "find" is a
 * literal value that must be included as part of the command line. This is
 * followed by two positional parameters that are substitution variables;
 * meaning that the text entered on the command line will be substituted for
 * these variables. The &lt;pattern&gt; and &lt;file&gt; operands hint at what
 * is expected from the user.</li>
 * <p>
 * <li>Operands are specified by a name enclosed in angle brackets.
 * 
 * <pre>
 *  &lt;operand&gt;
 *  &lt;another-operand&gt;
 *  &lt;with_underscore&gt;
 * </pre>
 * Operand names may include letters, digits, the underscore, and the dash "-".
 * No spaces are permitted. Generally, the dash and underscore are used to
 * separate words in the operand name.</li>
 * <p>
 * <li>Options come in two varieties, the short option and the long option. The
 * short option begins with a single dash followed by a single character (letter
 * or digit). The use of a digit as an option letter is discouraged to avoid
 * confusion with negative numbers supplied as arguments.
 * 
 * <pre>
 *  -a, -z, -7
 * </pre>
 * <li>The long option begins with two dashes "--" followed by a name composed
 * of letters digits, the underscore and the dash. Again the dash and underscore
 * usually separate words.
 * 
 * <pre>
 *  --all, --zebra, --data-set, --my_best_guess
 * </pre>
 * <li>
 * <li>Short options may be specified together after a single dash to create a
 * letter group. It is the same as specifying each of them separately.
 * 
 * <pre>
 *  -abc => -a -b -c
 * </pre>
 * </li>
 * <li>The last letter in a letter group may include an argument which may
 * itself be optional.
 * 
 * <pre>
 *  -abc &lt;arg&gt; => -a -b -c &lt;arg&gt;
 *  -abc [&lt;arg&gt;] => -a -b -c [&lt;arg&gt;]
 * </pre>
 * </li>
 * <h2>Posix Compliance</h2>
 * In an effort to be compliant with the POSIX standards, these are the rules
 * for single letter options. This standard is indicated by using the
 * ArgumentsPOSIX class.
 * <ul>
 * <li>An option begins with a single hypen or dash followed by a single
 * alpha-numeric character. -a</li>
 * <li>Options must precede the operands (positional arguments) on the command
 * line. In order to adhere to this restriction, this parser will stop after the
 * first operand that it encounters.</li>
 * <li>[-abc] is the same as [-a] [-b] [-c].</li>
 * <li>[-abc &lt;arg&gt;] is the same as [-a] [-b] [-c &lt;arg&gt;]</li>
 * <li>[-x [&lt;arg&gt;] specifies an optional argument which can only be
 * supplied by placing the argument adjacent to the option letter as in
 * -xArgument. If spaces appear after the option, the argument is not
 * recognized.</li>
 * </ul>
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 * 
 */
public class ArgFaceInfo {
}