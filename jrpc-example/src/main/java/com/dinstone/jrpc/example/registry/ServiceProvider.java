/*
 * Copyright (C) 2014~2016 dinstone<dinstone@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dinstone.jrpc.example.registry;

import java.io.IOException;
import java.util.Properties;

import com.dinstone.jrpc.api.Server;
import com.dinstone.jrpc.api.ServerBuilder;
import com.dinstone.jrpc.example.HelloService;
import com.dinstone.jrpc.example.HelloServiceImpl;

public class ServiceProvider {

    public static void main(String[] args) throws IOException {
        // Server server = new Server("-:4444");
        // Server server = new Server("-", 4444);
        // Server server = new Server("localhost", 4444);

        ServerBuilder builder = new ServerBuilder().bind("localhost", 4444);
        // setting endpoint config
        builder.endpointConfig().setEndpointId("provider-1").setEndpointName("example-registry-provider");

        // setting registry config
        Properties props = new Properties();
        props.setProperty("zookeeper.node.list", "localhost:2181");
        builder.registryConfig().setSchema("zookeeper").setProperties(props);

        // setting transport config
        props = new Properties();
        props.setProperty("rpc.handler.count", "2");
        builder.transportConfig().setSchema("mina").setProperties(props);

        Server server = null;
        try {
            // build server and start it
            server = builder.build().start();

            // export service
            server.exportService(HelloService.class, new HelloServiceImpl());

            System.in.read();
        } finally {
            if (server != null) {
                server.stop();
            }
        }
    }

}
