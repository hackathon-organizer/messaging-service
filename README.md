# Hackathon Organizer MESSAGING-SERVICE

Messeging service works as [WebSocket](https://en.wikipedia.org/wiki/WebSocket) signal server handler between clients and uses [REST API](https://en.wikipedia.org/wiki/Representational_state_transfer) to provide text chat messages.

# Responsibilities

- Works as a WebSocket signal server that handles peer connections and sessions
- Creates and manages [OpenVidu](https://openvidu.io/) sessions needed for voice/video chat and screen sharing
- Uses REST API to fetch messages that belong to certain chat room
- Saves chat messages to database

# Live demo

https://gentle-froyo-5a1b8e.netlify.app

# Setup

You can run Hackathon Organizer project using Docker compose. See [hackathon organizer infrastructure](https://github.com/hackathon-organizer/infrastructure) for more details.
