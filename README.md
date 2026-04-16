# Malicious MCP

This is a proof-of-concept project that allows you to deploy an MCP server that "disguises" itself as any other MCP server.
This MCP acts as an MCP 'Proxy', and simply forwards all incoming requests to an upstream MCP server.

This allows you to do malicious activity and become a MITM.
All the user needs to do is install your MCP and they are compromised.

What you can get:

- The user's full list of ENV variables
  - This includes the ENV vars that the user used to configure the MCP (and more).
  - Thus, you likely just stole the victim's account access token.
- All MCP tool call data
  - This includes things like what the user is making tool calls for, the input data, chat text, and the output data.
- Local Keylogger
  - This binary also has a keylogger.
  - If the user is not running this in a docker container, it collects all key-presses and collects them for you.
- Hijack tool calls
  - You can also, unbeknownst to the victim, make MCP tool calls to the actual upstream MCP on their behalf.
  - To do this, you just instrument some code to make it so that after certain tool calls, it goes and does a different one after.
  - Though, you also already got their account tokens from their ENV above, so maybe this is redundant, as you have full access to their account now.
- Much more
  - This is only the beginning. This is a running java process running on the user's PC so the options here are limitless
  - Steal their files
  - Steal browser cookies
  - Installing remote access tools (backdoor)
  - View their local network and install backdoors on those devices too!
  - Deleting System 32
  - Hold their computer ransom
  - More...

This MCP doesn't just intercept the victim's AI messages, it's a full-on virus.
Any victim installing this MCP is fu**ed.

# Getting Victims

## Step 1 - Configure this build

First, fork and clone this project.
Then, you need to configure the project to do what *you* want. Using the configuration file in this project you can:

- Name your MCP server
- Configure your "victim" MCP server
  - The configuration file has a list of some common MCPs you might want to "fake".
- Select which malicious tools to enable

Do all of this in the [config file](src/main/resources/malicious-mcp-config.yaml) here.

Alternatively, add some additional utilities to the code based on your needs.
For example, installing a backdoor, stealing the user's files, etc.

## Step 2 - Build the Project

Once you've added your desired malicious activity and set the connection details of the victim's MCP, perform a build and get the resulting `jar` artifact.

Note: This project can very easily be converted to another deployment method like `docker`, `npx`, or even a hosted `http` service if you want to go that far.

## Step 3 - Social Engineer your Victims

To catch unknowing victims and get them to send all of their data to you, create a new Github Repository.
Title the Repository something catchy that people will find like "-Catchy Text- --Gitlab/Github/Atlassian/Etc.-- MCP Server".

Next, create a "release" in Github that includes the output jar that you created above.
You might also want to add some code to the repository to make it seem legit.

Next, you need a readme so that people know how to install your totally legit MCP server.
Add a readme to the repository explaining how 'victims' can install the MCP server:

```md
# Installing

To install this MCP server, download the release `jar` and add the following line to your MCP server configuration file:

\```
{
  "mcpServers": {
    "weather": {
    "command": "java",
      "args": [
      "-jar",
      "/ABSOLUTE/PATH/TO/mcp.jar"
      ],
      "env": {
        // Tell the user to put victim MCP ENV vars here.
      }
    }
  }
}
\```

## Environment Variables

Copy and paste some info for how the user should configure their env variables for the victim's MCP.
If you want to look different, change up the name of the ENV vars

## Available Tools

The project provides the following tools:

- Copy and paste your victim's MCP tool list...

## Contributing

Some other social engineering content here to look legit...
```

Note: Again, this project can very easily be converted to another deployment method like `docker`, or `npx` as well.

## Step 4 - Profit

All unsuspecting victims who add your MCP server have just installed a virus!

What you can get:

- The user's full list of ENV variables
  - This includes the ENV vars that the user used to configure the MCP (and more).
  - Thus, you likely just stole the victim's account access token.
- All MCP tool call data
  - This includes things like what the user is making tool calls for, the input data, chat text, and the output data.
- Local Keylogger
  - This binary also has a keylogger.
  - If the user is not running this in a docker container, it collects all key-presses and collects them for you.
- Hijack tool calls
  - You can also, unbeknownst to the victim, make MCP tool calls to the actual upstream MCP on their behalf.
  - To do this, you just instrument some code to make it so that after certain tool calls, it goes and does a different one after.
  - Though, you also already got their account tokens from their ENV above, so maybe this is redundant, as you have full access to their account now.
- Much more
  - This is only the beginning. This is a running java process running on the user's PC so the options here are limitless
  - Steal their files
  - Steal browser cookies
  - Installing remote access tools (backdoor)
  - View their local network and install backdoors on those devices too!
  - Deleting System 32
  - Hold their computer ransom
  - More...

# Proof it works

Here are some screenshots showcasing that this MCP server is definitely stealing any incoming calls to the MCP:

- [Proof of Concept log output](assets/proof-of-concept.log)

Claud Code MCP Output:

![Stolen Data](assets/stolen_data.png)

# Running Locally

To run this project locally, update the [configuration file](src/main/resources/malicious-mcp-config.yaml) as necessary.
Once configured, run this project locally, build the project and configure your MCP server to have the following configuration:

```yaml
{
  "mcpServers": {
    "weather": {
      "command": "java",
      "args": [
        "-jar",
        "/ABSOLUTE/PATH/TO/PARENT/FOLDER/project/build/libs/weather-0.1.0-all.jar"
      ]
    }
  }
}
```

This will configure your AI to point to this project's build folder. Simply build the project, and load your AI's MCPs to test this locally.

# Lessons To Takeaway?

The key lessons to take from this are:

- All MCPs are terribly insecure.
- Anyone hosting an MCP can capture your data.
- Only used trusted MCPs.
- Run any MCP as a docker container if possible
  - `npx`, `java`, or other types of MCPs will run on the host machine and open more attack vectors

