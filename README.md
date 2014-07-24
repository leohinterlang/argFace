
ArgFace
===
Have you ever seen this kind of message from a computer program?

    Usage:
        program [-a] [-b|--brand] [-c <name>] [find <pattern>] <file>...
    Options:
        -a                 Process all files as a unit
        -b, --brand		   Brand each line
        -c          <name> Prefix name

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
    	  "Usage: program [-a] [-b|--brand] [-c <name>] [find <pattern>] <file>...";
    	private boolean aOption;
    	private boolean bOption;
    	private boolean cOption;
    	private String  cName;
    	private boolean findOperand;
    	private String  patternOperand;
    	private String [] fileOperand;
    	
		public static void main (String [] args) {
      		Program prog = new Program();
      		ArgFace argFace = ArgPrototype(usageText, prog);
      		if (argFace == null) {
      			System.exit(1);
      		}
      		int nArg = argFace.parse(args);
      		if (nArg < 0) {
      			System.exit(1);
      		}
      		if (aOption) {
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
If the option includes an alternative, as with the "-b|--brand" specification,
the variable can use either part as "bOption" or "brandOption".

The naming convention uses lower camel case throughout.
Thus, an option "--dir-path" would use the variable declaration:

	private boolean dirPathOption;

Anything in the usage text that is not an option is an operand.
Operands come in two flavors: literals and variables.
The literal operand "find" is followed by a variable operand "<pattern>".
The "<file>" specifier indicates a variable operand.

### Different Models
ArgFace uses various operational models that determine the way that information is passed
between the interface and the program. These include:
* **ArgPrototype** - Uses reflection to pass information.
* **ArgStandard** - Uses public getter and setter methods to pass information.
* **ArgProcedure** - Uses method calls to pass information.

The creation phase of ArgFace establishes which model will be used.
This is performed with code of the form:

    ArgFace argFace = <Model>.create(usageText, object);

* ArgPrototype uses reflection to access fields defined by the program.
The interface does not require getter and setter methods for this model but will
instead attempt to access even private fields of the program. Note, however, that
private access will fail if there is a *SecurityManager* in place for the program.

* ArgStandard also uses reflection to access fields. But unlike *ArgPrototype*, it
uses only public getter and setter methods to exchange information. This model may
be used even when the program is run with a *SecurityManager*.

* ArgProcedure does not use any reflection. In this model, information is exchanged
by making method calls to the interface. Although these methods are available to all
of the models, they are not always necessary.

Other command line interfaces (CLI) require a long sequence of procedural code
just to get the options and arguments to make sense.
ArgFace does away with all that clutter.
Rather than a load of initialization code, the usage text fully describes what is expected by the program.

