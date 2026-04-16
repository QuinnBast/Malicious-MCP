# Malicious MCP

NOTE: This is not meant to be used maliciously. This is a proof-of-concept intended to learn what is possible
if you use an MCP server you do not trust.

This is a proof-of-concept project that allows you to deploy an MCP server that "disguises" itself as any other MCP server.
This works by acting as an MCP client, and this MCP just forwards on requests to that MCP server.

This allows you to do malicious activity like steal the user's input prompts, or, act on behalf of the user at their destination MCP without them knowing.

All the user needs to do is install your MCP and anytime a tool call goes through your MCP server, you can intercept it.

# Getting Victims

## Step 1 - Configure this build

First, configure the build in this project to point to the "victim" MCP server that you are going to masquerade as.
Do some reading on their MCP server, and configure the MCP command in the [config file](src/main/resources/malicious-mcp-config.yaml).

The MCP server will likely need some ENV variables.
You do not need to know your victim's tokens, simply list the names of the ENV variables that you are going to need to extract from the user.

Then, configure the name of your desired MCP and it's version number. Also choose a malicious behavior, or, add your own!

The configuration file has a list of some common MCPs you might want to "fake".

## Step 2 - Build the Project

Once you've added your desired malicious activity and configure the connection details for your victim's MCP, perform a build and get the resulting `jar` artifact.

Note: This project van very easily be converted to another deployment method like `docker`, `npx`, or even a hosted `http` service if you want to go that far.

## Step 3 - Social Engineer your Victims

To catch unknowing victims and get them to send all of their data to you, create a new Github Respository.
Title the Repository something catchy that people will find like "-Catchy Text- --Gitlab/Github/Atlassian/Etc.-- MCP Server".

Next, create a "release" in github with the output jar that you created above.
You might also want to add some code to the repository to make it seem legit.

Next, you need a readme so that people know how to install your totally legit MCP server.
Add a readme to the repository explaining how 'victims' can install the MCP server:

```md
# Installing

To install this MCP server, add the following line to your MCP server configuration file:

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
        // Tell the user to put their ENV vars here.
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

Note: Again, this project van very easily be converted to another deployment method like `docker`, or `npx` as well.

## Step 4 - Profit

Profit, as all unsuspecting victims configure their MCP servers which will forward a ton of data to you!

What you can do:

- The user's full list of ENV variables
  - This includes the ENV vars that the user used to configure the MCP (and more).
  - Thus, you likely just stole the victim's account access token.
- All MCP tool call data
  - This includes things like what the user is making tool calls for, the input data, and the output data.
- Local Keylogger
  - This binary also has a keylogger.
  - If the user is not running this in a docker container, it collects all keypresses and intercepts them.
- Hijack tool calls
  - You can also, unbeknownst to the victim, make MCP tool calls to the actual upstream MCP on their behalf.
  - To do this, you just instrument some code to make it so that after certain tool calls, it goes and does a different one after.
  - Though, you also already got their account tokens from above, so maybe this is redundant, as you can now just use their token.
- Much more
  - This is only the beginning. This is a java process running on the user's PC so the options here are limitless
  - Navigating files
  - Stealing browser cookies
  - Installing remote access tools
  - Deleting System 32
  - More...

# Proof it works

Here are some screenshots showcasing that this MCP server is definitely stealing any incoming calls to the MCP:



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

