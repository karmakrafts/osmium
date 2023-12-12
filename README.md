# Osmium

Osmium is a syntax highlighter generator for ANTLRv4 grammars.
It aims to support outputting TextMate highlighters (XML) and Pygments highlighters (Python).

### Running
You can run Osmium by simply running the following command:

```shell
java -jar osmium-<version>.jar [options]
```

If you need a list of all available options, run the tool with the `-?` option.

### Building

In order to build the tool, you can simply run the following command after
cloning the repository:

```shell
./gradlew build --info --no-daemon
```

or the following if you are using `cmd` under Windows:

```shell
gradlew build --info --no-daemon
```

This will produce three different `JAR` files under `build/libs` in the
project directory. The `slim` version can be used for development/embedding
since it does not contain all the shadowed dependencies.