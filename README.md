# What is this repository?
This repository is a study project of three people, which final target is to create a flexible API and use it to solve a realistic task of developing, implementing and testing a multi-platform chat bot.
The repository is divided to two parts - the bot which relies on the api and the api itself.

# Global requirements
- JDK 16 or higher (16.0.2 is used in development)
- Gradle 7.1

# Understanding and running the bot
- To compile the bot, you will need to meet global requirements; You can install JDK 16 [here](openjdk.java.net) And Gradle 7.1 [here](https://services.gradle.org/distributions/gradle-7.1-bin.zip)
- Run the bot with `gradle run` command
- This bot uses [jservice.io](jservice.io) API to get trivia questions
- Inside the bot, you can start a quiz with `/quiz` command and type the number of questions for the test in the next message
- The bot will ask this count of questions to you and check your answers
- You will get statistics after the quiz is over

# Understanding and using javabot-example API
## Warning
As of now, this bot can only understand and reply with **TEXT MESSAGES**!

## Functionality
This bot can handle users' messages in multiple contexts (called State)

## Bot creation
### Fist bot example:
```java
var token = "???"; // You better put it to enviroment variables
var runtimeDb = new RuntimeDb("DefaultState"); // [built-in to api] just a hashmap with getters/setters
var tgBot = new TgTransport(TOKEN); // io for a scenario

// Create a scenario
new Scenario()
    // Add states (one for now)
    .addState(
        new State("DefaultState")
            .addCommandHandler("/start", ctx -> ctx.reply("This bot copies messages. You can also use /help."))
            .addCommandHandler("/help", ctx -> ctx.reply("Type some text and bot will send it back to you."))
            .addContextHandler(ctx -> ctx.notYetReplied() ? ctx.reply(ctx.newMessage.getMessageText()) : ctx)
    )
    
    // Connect transports
    .addTransport(tgBot)
    
    // Init
    .initWithStore(runtimeDb);
```

### Summary
To create and run a bot, you should:
- Init a data store (`IStore` interface)
- Init transports (`ITransport` interface, possibly `CommonTransport` abstract class)
- Create a state scenario (`Scenario` class)
- Create states for this scenario (`State` class, `addState` method of `Scenario` class)
- Bind handlers for each of states (`addContextHandler` method of `State` class)
- Connect transports for scenario to run on (with `addTransport(ITransport transport)`)
- Connect a data store to let the bot manage users' states by name and start the bot (with `initWithStore(IStore store)`)

To program bot behavior, you change it's `StateScenario`.
To achieve the desired result effectively, you can:
- Create new states:
```java
new Scenario()
    .addState(new State("DefaultState"))
    .addState(new State("AnotherState"));
```
- Bind new handlers (command handlers and context handlers):
```java
new Scenario()
    .addState(
        new State("DefaultState")
            .addContextHandler(ctx -> ctx.reply("first reply"))
            .addCommandHandler("/command", ctx -> ctx.reply("command triggered"))
            .addContextHandler(ctx -> ctx.reply("second or third reply"))
    )
    .addState(
        new State("AnotherState")
            .addCommandHandler("/exit", ctx -> reply("erm, ok..."))
    );
```
- Switch between states:
```java
new Scenario()
    .addState(
        new State("DefaultState")
            .addContextHandler(ctx -> ctx.reply("first reply"))
  /* !!! */ .addCommandHandler("/command", ctx -> ctx.reply("command triggered").setState("AnotherState"))
            .addContextHandler(ctx -> ctx.reply("second or third reply"))
    )
    .addState(
        new State("AnotherState")
  /* !!! */ .addCommandHandler("/exit", ctx -> ctx.reply("erm, ok...").setState("DefaultState"))
    );
```

...And, when possible, split complex handlers to multiple handlers or even states.

## Main instances
### Transport
Contains three abstraction layers:
- `ITransport` (the interface)
- `CommonTransport` (the abstract class; it has common features that are needed in most of `ITransport` implementations)
- `implementations/*` (e.g. `TgTransport` - implementations of `ITransport`, possibly derived from `CommonTransport`. Used for IO for scenarios and are useless in all other cases; used to implement `State` functionality before `Scenario` class was created.)
### EventContext
Is a type for unified event data; all `ITransport` implementations should have their own `toEventContext(T update)` implementation.
This type also has:
- references to a current `IStore store` for state management and to a transport which created this EventContext object
- `EventContext reply(String msg)` method - reply with a message and return the context
- `setState(String stateName)` method - set a new state in `store` for current user and return the context
- `notYetReplied()` method - true if some handler already replied in this context (there's a flag for it) and false otherwise
 
### ContextHandler
Is equal to `Function<EventContext, EventContext>` interface.
As for use cases, this means that `addContextHandler` and `addCommandHandler` can recieve
- A lambda expression: `ctx -> ctx` or `ctx -> ctx.reply(...).reply(...).setState(...).reply(...)`
- A static method: `static EventContext awesomeHandler(EventContext ctx) { return ctx.(...); }` by referencing it like `ClassName::awesomeHandler`

### Scene
Unites context handlers. You can add:
- command handlers: `addCommandHandler(String commandText, ContextHandler handler)` (though the slash is not obligatory), they trigger when the user message **equals** the command text
- context handlers: `addContextHandler(ContextHandler handler)` - those will always trigger (except for when the state is different), and the conditions of doing or not doing something should be stated inside the handler.

### Scenario
Unites user states' store, states, and transports and starts the bot.
The methods are:
- `addState(State state)`
- `addTransport(ITransport transport)` - binds an `ITransport`-implemented instance for input and output by `userId`
- `initWithStore(IStore store)` - sets a store (which will later be passed to all EventContexts inside this scenario) and starts the bot.

## Recommended simplifications
### Handler factories
- Standard: `ContextHandler CommonHandlers.reply(String msg)` - returns `ctx -> ctx.reply(msg)`
- Standard: `ContextHandler CommonHandlers.replyAndSetState(String msg, String newStateName)` - returns `ctx -> ctx.reply(msg).setState(newStateName)`
- Specific: `ContextHandler CommonHandlers.notAnsweredThenCopy()` - returns `ctx -> ctx.notYetReplied() ? ctx.reply(ctx.newMessage.getMessageText()) : ctx`

Example:
```java
new Scenario()
    .addState(
        new State("DefaultState")
            .addContextHandler(reply("first reply"))
            .addCommandHandler("/command", replyAndSetState("command triggered", "AnotherState"))
            .addContextHandler(reply("Still second or third reply"))
    )
    .addState(
        new State("AnotherState")
            .addCommandHandler("/exit", replyAndSetState("erm, ok...", "DefaultState"))
    );
```
###
