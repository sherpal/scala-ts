# scala-ts

The [Scala-js](https://www.scala-js.org/) to 
TypeScript `.d.ts` generator.

This projects aims to take a Scala-js source file, 
with classes, objects, methods, and members that have
been exported to JavaScript, and create the relevant
declaration file for TypeScript.

## Type inference

We don't do that. If a return type of a method, or
the type of a member is not explicitly specified,
we simply set `any`.

## What is currently working

When running `run filename.scala`, the program will
read the source file named `filename.scala`. For each
class, it will print a valid TypeScript declaration
with all exported methods and vals. 

## TODO

Everything needs to be rethought but the basic idea
will hold.

## Final goal

The goal is to have a tool that will read the entire
content of a Scala-js project, and generate one big
TypeScript declaration file suitable for using the
JavaScript generated file within TypeScript.
