/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.sshj.config;

import java.net.UnknownHostException;

import org.jclouds.domain.Credentials;
import org.jclouds.net.IPSocket;
import org.jclouds.ssh.SshClient;
import org.jclouds.sshj.SshjSshClient;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests the ability to configure a {@link SshjSshClient}
 * 
 * @author Adrian Cole
 */
@Test
public class SshjSshClientModuleTest {

   public void testConfigureBindsClient() throws UnknownHostException {

      Injector i = Guice.createInjector(new SshjSshClientModule());
      SshClient.Factory factory = i.getInstance(SshClient.Factory.class);
      SshClient connection = factory.create(new IPSocket("localhost", 22), new Credentials("username", "password"));
      assert connection instanceof SshjSshClient;
   }
}
