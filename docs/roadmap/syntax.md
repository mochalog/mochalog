## Mochalog Syntax

### Goals - API Simplicity and Intuitiveness
Consider a simple example, where the file `school.pl` defines the predicate `teaches(x, y)` to mean *x teaches y*.

#### JPL - Java to Prolog Example ####
```java
// Equivalent to the SWI-Prolog query
// ? teaches(aristotle, X)
Variable X = new Variable();
Query q = new Query("teaches", new Term[] {new Atom("aristotle"), X});
while (q.hasMoreElements()) {
    Hashtable binding = (Hashtable) q.nextElement();
    Term t = (Term) binding.get(X);
    System.out.println(t);
}
```

#### Mochalog - Java to Prolog Example ####
Now to demonstrate how I envision the same query could be performed in Mochalog through a more convenient and intuitive syntax.

```java
// Equivalent to the SWI-Prolog query
// ? teaches(aristotle, X)
import pl.school; // Directly import from the .pl file

Variable X = new Variable();
Query q = school.teaches("aristotle", X);

while (!q.isFinished())
{
    System.out.println(X);
    query.continue();
}
```

In the previous example, `Query` is a `Future<Variable>` which uses Java's Futures library to leverage multithreading to our advantage. While we do processing on the current variable binding, Mochalog could be doing background processing and continuing the query to be able to rapidly serve up results when requested by the client.
