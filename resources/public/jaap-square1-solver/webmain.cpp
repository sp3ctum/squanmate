// The cheerp/clientlib.h header contains declarations
// for all the browser APIs.
#include <cheerp/clientlib.h>
#include <cheerp/client.h>
#include "EngineTwst.h"
#include "main.h"

using namespace std;
using namespace client;

// The class can of course have any name
// The [[jsexport]] attribute tells Cheerp to make
// the class available to JavaScript code
class [[cheerp::jsexport]] Square1Solver
{


 private:
  // The class is allowed to have member variables
  // but they should all be trivially destructible
  int depth = 20;
  int mode = -2;
  ostream* output=NULL; // not used when reading from input position

 public:
  Square1Solver() {
  }

  client::String* solve()
  {
    Engine* eng = (Engine*) new EngineTwst();

    // client is a C++ namespace that contains all browser APIs
    client::console.log("Initializing Jaap Scherpuis's Square-1 solver, based on code available from https://www.jaapsch.net/puzzles/square1.htm");

    //initialise its pruning tables
    eng->Initialise();
    eng->InitPermTable();

    char* input = "A2B3C1D45E6F7G8H"; // example 2 in readme
    Position1 position;
    ReadPosition(input, position);
    position.IgnoreMiddle();

    eng->DoSearch(position,depth,mode,output);

    return new client::String(eng->solution.c_str());
  }
};

// webMain is the entry point for web applications written in Cheerp.
// An entry point, even if empty, is still required
void webMain()
{
}