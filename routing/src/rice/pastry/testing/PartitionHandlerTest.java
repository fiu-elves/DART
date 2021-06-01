/*******************************************************************************

"FreePastry" Peer-to-Peer Application Development Substrate

Copyright 2002-2007, Rice University. Copyright 2006-2007, Max Planck Institute 
for Software Systems.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

- Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

- Neither the name of Rice  University (RICE), Max Planck Institute for Software 
Systems (MPI-SWS) nor the names of its contributors may be used to endorse or 
promote products derived from this software without specific prior written 
permission.

This software is provided by RICE, MPI-SWS and the contributors on an "as is" 
basis, without any representations or warranties of any kind, express or implied 
including, but not limited to, representations or warranties of 
non-infringement, merchantability or fitness for a particular purpose. In no 
event shall RICE, MPI-SWS or contributors be liable for any direct, indirect, 
incidental, special, exemplary, or consequential damages (including, but not 
limited to, procurement of substitute goods or services; loss of use, data, or 
profits; or business interruption) however caused and on any theory of 
liability, whether in contract, strict liability, or tort (including negligence
or otherwise) arising in any way out of the use of this software, even if 
advised of the possibility of such damage.

*******************************************************************************/ 
package rice.pastry.testing;

import java.io.IOException;
import java.net.InetSocketAddress;

import rice.environment.Environment;
import rice.pastry.NodeHandle;
import rice.pastry.PastryNode;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.PartitionHandler;
import rice.pastry.standard.RandomNodeIdFactory;

public class PartitionHandlerTest {

  private static final int PORT_A = 2323;
  private static final int PORT_B = 4646;

  /**
   * @param args
   * @throws IOException 
   * @throws InterruptedException 
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    Environment env;
    env = new Environment();
    env.getParameters().setString("loglevel", "ALL");
    SocketPastryNodeFactory factoryA = new SocketPastryNodeFactory(new RandomNodeIdFactory(env), PORT_A, env);
    SocketPastryNodeFactory factoryB = new SocketPastryNodeFactory(new RandomNodeIdFactory(env), PORT_B, env);
    
    NodeHandle bootstrapA = factoryA.getNodeHandle(new InetSocketAddress("localhost", PORT_A));
    PastryNode a = factoryA.newNode(bootstrapA);
    NodeHandle bootstrapB = factoryB.getNodeHandle(new InetSocketAddress("localhost", PORT_B));
    PastryNode b = factoryB.newNode(bootstrapB);
    
    PartitionHandler handlerA = new PartitionHandler(a, factoryA, null);
    PartitionHandler handlerB = new PartitionHandler(b, factoryB, null);
    
    // wait for a to boot
    // wait for b to boot
    
    synchronized (a) {
      a.wait(30000);
    }
    
    handlerA.rejoin(factoryA.getNodeHandle(new InetSocketAddress("localhost", PORT_B)));
    
  }

}