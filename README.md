# blockchain-mesh-network
adapting block chain with mesh networks for secure, decentralized communication

## Resource Request Protocol
1. A node, an organization center (maybe a fire station, relief center, warehouse, etc.) at some geographic location, makes a "resource request" in a mesh network
2. All network nodes receive the "resource request". Those who wish to satisfy the request submit a "resource request bid" (which is an offering of resources)
3. The requesting node compiles all "resource request bid"'s it has received and submits a "resource agreement" (tagging the requestID and BidID)
4. All network nodes receive the "resource agreement"
5. node that is fulfilling the order submits a "resource sent" and the node receiving the resources submits a "resource received".
6. All message types are stored in the blockchain 
					and are signed by the sender (not implemented yet)

##Message Types
type - what it's for (parameter arguments)
1. ResourceRequest - initial request (type, unit, amount)
2. ResourceRequestBid - initial offer (type, unit, amount, resourceRequestID)
3. ResourceAgreement - accountable agreement (type, unit, amount, resourceRequestBidID)
4. ResourceSent - transfer status update (ResourceAgreementID)
5. ResourceReceived - transfer status update - completes transaction (resourceSentID)

## BluetoothGUI
A stand alone application that manages one Node instance, which, in turn, manages a Blockchain object and a BluetoothManager object.
A user can use this application to:
	1. request resources from a network it is connected to
	2. see the status of resource requests (bid, agreement, sent, received)

### GUI Flow logic
1. When the "Request Resources" button is hit, a ResourceRequest message object is instantiated with arguments taken from the GUI form and given to myNode via the add method
2. myNode accepts the message, passes a copy of it to its blockchain instance and sorts the original into one of five ArrayLists (one for each message type)
3. The buttons "Generate Bid" and "Accept Bid" operate in similar ways by creating and passing ResourceRequestBid and ResourceAgreement Objects to myNode, respectively.

###Node
Node is an interface implemented by the classes, SimulationNode (simulation testing) and NetworkNode (real world).
NetworkNode holds all the data for the BluetoothGUI.

### Blockchain
Solves for blocks, verifies received blocks, and adds blocks to the blockchain.

#### Block
A block's hash acts as its ID
stores a list of messages

### BluetoothManager
creates a bluetooth client and bluetooth server for block and message communication over extremely short range.
