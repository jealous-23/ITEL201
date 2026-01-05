package finalproject.gamestate;

import java.util.Random;

public class QuestionGenerator {
	private static Question[] getEasyQuestions() {
	    return new Question[] {
	        new Question("Which keyword defines a subclass in Java?", new String[]{"implements", "extends", "sub", "inherits"}, "extends"),
	        new Question("Which method is the entry point of a Java program?", new String[]{"run()", "start()", "main()", "init()"}, "main()"),
	        new Question("Which symbol ends a Java statement?", new String[]{".", ":", ";", ","}, ";"),
	        new Question("Which keyword is used to print output?", new String[]{"print", "println", "System.out.println", "echo"}, "System.out.println"),
	        new Question("Which data type stores whole numbers?", new String[]{"float", "double", "int", "boolean"}, "int"),
	        new Question("Which data type stores true or false?", new String[]{"int", "String", "boolean", "char"}, "boolean"),
	        new Question("Which operator is used for addition?", new String[]{"+", "-", "*", "/"}, "+"),
	        new Question("Which keyword creates an object?", new String[]{"class", "object", "new", "make"}, "new"),
	        new Question("Which loop runs at least once?", new String[]{"for", "while", "do-while", "foreach"}, "do-while"),
	        new Question("Which keyword stops a loop?", new String[]{"exit", "break", "stop", "return"}, "break"),
	        new Question("Which keyword skips one loop iteration?", new String[]{"skip", "next", "continue", "pass"}, "continue"),
	        new Question("Which type stores decimal numbers?", new String[]{"int", "boolean", "double", "char"}, "double"),
	        new Question("What is the default value of an object reference?", new String[]{"0", "false", "null", "undefined"}, "null"),
	        new Question("Which operator compares values?", new String[]{"=", "==", "!=", "equals"}, "=="),
	        new Question("Which class represents text?", new String[]{"Text", "String", "Char", "Word"}, "String"),
	        new Question("Which package is imported by default?", new String[]{"java.util", "java.lang", "java.io", "java.net"}, "java.lang"),
	        new Question("Which keyword defines a constant?", new String[]{"static", "const", "final", "fixed"}, "final"),
	        new Question("Which method gets String length?", new String[]{"size()", "count()", "length()", "getLength()"}, "length()"),
	        new Question("Which keyword refers to current object?", new String[]{"this", "self", "current", "me"}, "this"),
	        new Question("Which loop is best when count is known?", new String[]{"while", "do-while", "for", "foreach"}, "for"),

	        // filler EASY to reach 50
	        new Question("Which symbol starts a comment?", new String[]{"//", "##", "/*", "--"}, "//"),
	        new Question("Which keyword defines a class?", new String[]{"define", "struct", "class", "object"}, "class"),
	        new Question("Which data type stores a character?", new String[]{"String", "char", "int", "boolean"}, "char"),
	        new Question("Which operator multiplies numbers?", new String[]{"*", "+", "/", "-"}, "*"),
	        new Question("Which keyword exits a method?", new String[]{"stop", "exit", "return", "break"}, "return"),
	        new Question("Which keyword imports a package?", new String[]{"using", "require", "import", "include"}, "import"),
	        new Question("Which array index starts first?", new String[]{"1", "-1", "0", "depends"}, "0"),
	        new Question("Which operator divides numbers?", new String[]{"/", "%", "*", "+"}, "/"),
	        new Question("Which method prints without newline?", new String[]{"print()", "println()", "echo()", "write()"}, "print()"),
	        new Question("Which keyword defines inheritance?", new String[]{"implements", "extends", "inherits", "parent"}, "extends"),
	        new Question("Which operator checks NOT equal?", new String[]{"==", "!=", "<>", "="}, "!="),
	        new Question("Which keyword is used for decision making?", new String[]{"if", "when", "choose", "select"}, "if"),
	        new Question("Which keyword defines a package?", new String[]{"import", "package", "module", "namespace"}, "package"),
	        new Question("Which loop checks condition first?", new String[]{"do-while", "while", "foreach", "switch"}, "while"),
	        new Question("Which keyword defines a method?", new String[]{"func", "method", "void", "def"}, "void"),
	        new Question("Which keyword handles multiple choices?", new String[]{"if", "else", "switch", "case"}, "switch"),
	        new Question("Which keyword represents no value?", new String[]{"empty", "null", "void", "none"}, "null"),
	        new Question("Which keyword defines multiple choices?", new String[]{"case", "switch", "if", "else"}, "switch"),
	        new Question("Which data type stores true/false?", new String[]{"int", "char", "boolean", "String"}, "boolean"),
	        new Question("Which symbol ends a block?", new String[]{"}", "]", ")", ";"}, "}")
	    };
	}
	
	private static Question[] getMediumQuestions() {
	    return new Question[] {
	        new Question("Which principle hides data?", new String[]{"Inheritance", "Encapsulation", "Abstraction", "Polymorphism"}, "Encapsulation"),
	        new Question("Which access modifier allows subclass access?", new String[]{"private", "default", "protected", "public"}, "protected"),
	        new Question("Can abstract classes have constructors?", new String[]{"Yes", "No", "Sometimes", "Only static"}, "Yes"),
	        new Question("Which collection disallows duplicates?", new String[]{"List", "Set", "Map", "Queue"}, "Set"),
	        new Question("Which Map allows one null key?", new String[]{"HashMap", "Hashtable", "TreeMap", "ConcurrentHashMap"}, "HashMap"),
	        new Question("Which keyword prevents overriding?", new String[]{"static", "private", "final", "abstract"}, "final"),
	        new Question("What does JVM stand for?", new String[]{"Java Virtual Machine", "Java Variable Machine", "Joint VM", "Java Visual Machine"}, "Java Virtual Machine"),
	        new Question("Which binding is used in overriding?", new String[]{"Static", "Dynamic", "Early", "Compile-time"}, "Dynamic"),
	        new Question("Which interface enables lambda?", new String[]{"Runnable", "Marker", "Functional", "Serializable"}, "Functional"),
	        new Question("Which exception is unchecked?", new String[]{"IOException", "SQLException", "NullPointerException", "FileNotFound"}, "NullPointerException"),

	        // expanded MEDIUM
	        new Question("Which keyword throws exception?", new String[]{"throw", "throws", "catch", "finally"}, "throw"),
	        new Question("Which block always runs?", new String[]{"try", "catch", "finally", "throw"}, "finally"),
	        new Question("Which collection maintains insertion order?", new String[]{"HashSet", "TreeSet", "LinkedHashSet", "Set"}, "LinkedHashSet"),
	        new Question("Which class iterates collections?", new String[]{"Looper", "Iterator", "Stepper", "Walker"}, "Iterator"),
	        new Question("Which keyword defines interface usage?", new String[]{"extends", "implements", "inherits", "uses"}, "implements"),
	        new Question("Which class is root of all Java classes?", new String[]{"System", "Object", "Class", "Base"}, "Object"),
	        new Question("Which keyword calls parent constructor?", new String[]{"this()", "parent()", "super()", "base()"}, "super()"),
	        new Question("Which operator compares references?", new String[]{"equals()", "==", "!=", "compare"}, "=="),
	        new Question("Which keyword prevents serialization?", new String[]{"static", "final", "transient", "volatile"}, "transient"),
	        new Question("Which class handles mutable strings?", new String[]{"String", "StringBuilder", "CharSequence", "Text"}, "StringBuilder"),

	        // fillers to reach 60
	        new Question("Which Map is synchronized?", new String[]{"HashMap", "Hashtable", "TreeMap", "LinkedHashMap"}, "Hashtable"),
	        new Question("Which interface does ArrayList implement?", new String[]{"Set", "Queue", "List", "Map"}, "List"),
	        new Question("Which keyword ensures thread safety?", new String[]{"volatile", "synchronized", "static", "final"}, "synchronized"),
	        new Question("Which keyword prevents inheritance?", new String[]{"static", "private", "final", "abstract"}, "final"),
	        new Question("Which exception handles file IO?", new String[]{"RuntimeException", "IOException", "NullPointerException", "Error"}, "IOException"),
	        new Question("Which class reads user input?", new String[]{"Input", "Reader", "Scanner", "System"}, "Scanner"),
	        new Question("Which keyword defines abstract method?", new String[]{"void", "abstract", "static", "final"}, "abstract"),
	        new Question("Which collection allows key-value?", new String[]{"List", "Set", "Map", "Queue"}, "Map"),
	        new Question("Which structure follows FIFO?", new String[]{"Stack", "Queue", "Set", "Map"}, "Queue"),
	        new Question("Which structure follows LIFO?", new String[]{"Queue", "Stack", "List", "Set"}, "Stack")
	    };
	}
	
	private static Question[] getHardQuestions() {
	    return new Question[] {
	        new Question("What causes deadlock?", new String[]{"Memory leak", "Circular lock dependency", "GC failure", "Overflow"}, "Circular lock dependency"),
	        new Question("Which memory stores objects?", new String[]{"Stack", "Heap", "Metaspace", "Register"}, "Heap"),
	        new Question("Which memory stores class metadata?", new String[]{"Stack", "Heap", "Metaspace", "CPU"}, "Metaspace"),
	        new Question("Which keyword ensures visibility?", new String[]{"static", "final", "volatile", "synchronized"}, "volatile"),
	        new Question("What is race condition?", new String[]{"Thread crash", "Shared state conflict", "Deadlock", "Exception"}, "Shared state conflict"),
	        new Question("Which GC manages memory?", new String[]{"Compiler", "Interpreter", "Garbage Collector", "Loader"}, "Garbage Collector"),
	        new Question("Which collection is thread-safe?", new String[]{"HashMap", "ArrayList", "ConcurrentHashMap", "HashSet"}, "ConcurrentHashMap"),
	        new Question("What is method hiding?", new String[]{"Overriding static", "Private override", "Shadowing", "Abstraction"}, "Overriding static"),
	        new Question("Which breaks equals/hashCode contract?", new String[]{"Immutable fields", "Mutable keys", "Final class", "Static fields"}, "Mutable keys"),
	        new Question("Which Java version introduced lambdas?", new String[]{"Java 7", "Java 8", "Java 9", "Java 10"}, "Java 8"),
	        // Additional questions added below
	        new Question("Which design pattern creates a single instance?", new String[]{"Factory", "Observer", "Singleton", "Decorator"}, "Singleton"),
	        new Question("Which keyword prevents inheritance?", new String[]{"static", "abstract", "final", "private"}, "final"),
	        new Question("What is the parent of all classes?", new String[]{"Object", "Class", "System", "Void"}, "Object"),
	        new Question("Which stream operation is lazy?", new String[]{"forEach", "collect", "filter", "count"}, "filter"),
	        new Question("Which interface for functional programming?", new String[]{"Serializable", "Runnable", "Comparator", "Function"}, "Function"),
	        new Question("What does transient keyword do?", new String[]{"Prevent overriding", "Prevent serialization", "Prevent GC", "Prevent instantiation"}, "Prevent serialization"),
	        new Question("Which type of inner class can access all members?", new String[]{"Static nested", "Anonymous", "Method local", "Member inner"}, "Member inner"),
	        new Question("What is checked exception?", new String[]{"Runtime error", "Syntax error", "Compile time error", "Logic error"}, "Compile time error"),
	        new Question("Which method do threads call?", new String[]{"start()", "run()", "execute()", "init()"}, "run()"),
	        new Question("Which class is immutable?", new String[]{"StringBuilder", "Date", "String", "ArrayList"}, "String"),
	        new Question("Which block runs always?", new String[]{"try", "catch", "finally", "throw"}, "finally"),
	        new Question("What is JVM responsible for?", new String[]{"Compiling code", "Running code", "Writing code", "Debugging code"}, "Running code"),
	        new Question("Which access modifier is default?", new String[]{"public", "private", "protected", "package-private"}, "package-private"),
	        new Question("Which data structure is LIFO?", new String[]{"Queue", "ArrayList", "Stack", "HashMap"}, "Stack"),
	        new Question("What does volatile guarantee?", new String[]{"Atomicity", "Visibility", "Ordering", "Synchronization"}, "Visibility"),
	        new Question("Which method in Thread causes a pause?", new String[]{"stop()", "wait()", "notify()", "yield()"}, "wait()"),
	        new Question("What is the result of 10 / 0?", new String[]{"0", "Infinity", "Exception", "NaN"}, "Exception"),
	        new Question("Which stream operation is terminal?", new String[]{"map", "filter", "sorted", "collect"}, "collect"),
	        new Question("Which interface has default methods?", new String[]{"Runnable", "Serializable", "List", "Comparator"}, "List"),
	        new Question("Which annotation marks functional interface?", new String[]{"@Override", "@Deprecated", "@FunctionalInterface", "@Singleton"}, "@FunctionalInterface")
	    };
	}

    public static Question getRandomQuestion(GameManager.Difficulty difficulty) { 	
        Random rand = new Random();
        return switch (difficulty) {
             case EASY -> getEasyQuestions()[rand.nextInt(getEasyQuestions().length)];
             case MEDIUM -> getMediumQuestions()[rand.nextInt(getMediumQuestions().length)];
             default -> getHardQuestions()[rand.nextInt(getHardQuestions().length)];
        };
    }
    
}