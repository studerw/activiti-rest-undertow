/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.studerw.activiti;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;


/**
 * @author Joram Barrez
 */
public class DemoDataGenerator {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DemoDataGenerator.class);

    protected transient ProcessEngine processEngine;
    protected transient IdentityService identityService;
    protected transient RepositoryService repositoryService;

    protected boolean createDemoUsersAndGroups;

    public void init() {

        this.identityService = processEngine.getIdentityService();
        this.repositoryService = processEngine.getRepositoryService();

        if (createDemoUsersAndGroups) {
            LOGGER.info("Initializing demo groups");
            initDemoGroups();
            LOGGER.info("Initializing demo users");
            initDemoUsers();
        }

    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public void setCreateDemoUsersAndGroups(boolean createDemoUsersAndGroups) {
        this.createDemoUsersAndGroups = createDemoUsersAndGroups;
    }

    protected void initDemoGroups() {
        String[] assignmentGroups = new String[]{"management", "sales", "marketing", "engineering", "human-resources"};
        for (String groupId : assignmentGroups) {
            createGroup(groupId, "assignment");
        }

        String[] securityGroups = new String[]{"user", "admin"};
        for (String groupId : securityGroups) {
            createGroup(groupId, "security-role");
        }
    }

    protected void createGroup(String groupId, String type) {
        if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
            Group newGroup = identityService.newGroup(groupId);
            newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
            newGroup.setType(type);
            identityService.saveGroup(newGroup);
        } else {
            LOGGER.debug("Demo group: {} already exists - not creating", groupId);
        }
    }

    protected void initDemoUsers() {
        createUser("kermit", "Kermit", "The Frog", "kermit", "kermit@activiti.org",
                "org/activiti/explorer/images/kermit.jpg",
                Arrays.asList("management", "sales", "marketing", "engineering", "user", "admin"),
                Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
                        "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));

        createUser("gonzo", "Gonzo", "The Great", "gonzo", "gonzo@activiti.org",
                "org/activiti/explorer/images/gonzo.jpg",
                Arrays.asList("management", "sales", "marketing", "user"),
                null);
        createUser("fozzie", "Fozzie", "Bear", "fozzie", "fozzie@activiti.org",
                "org/activiti/explorer/images/fozzie.jpg",
                Arrays.asList("marketing", "engineering", "user"),
                null);
        createUser("cookie-monster", "Cookie", "Monster", "cookie-monster", "cookie-monster@activiti.org",
                null,
                Arrays.asList("sales", "engineering", "user"),
                null);
        createUser("miss-piggy", "Miss", "Piggy", "miss-piggy", "miss-piggy@activiti.org",
                null,
                Arrays.asList("human-resources", "user"),
                null);
    }

    protected void createUser(String userId, String firstName, String lastName, String password,
                              String email, String imageResource, List<String> groups, List<String> userInfo) {

        if (identityService.createUserQuery().userId(userId).count() == 0) {

            // Following data can already be set by demo setup script

            User user = identityService.newUser(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            user.setEmail(email);
            identityService.saveUser(user);

            if (groups != null) {
                for (String group : groups) {
                    identityService.createMembership(userId, group);
                }
            }
        } else {
            LOGGER.debug("User {} already exists - not creating.", userId);
        }

        // Following data is not set by demo setup script

        // image
        if (imageResource != null) {
            byte[] pictureBytes = IoUtil.readInputStream(this.getClass().getClassLoader().getResourceAsStream(imageResource), null);
            Picture picture = new Picture(pictureBytes, "image/jpeg");
            identityService.setUserPicture(userId, picture);
        }

        // user info
        if (userInfo != null) {
            for (int i = 0; i < userInfo.size(); i += 2) {
                identityService.setUserInfo(userId, userInfo.get(i), userInfo.get(i + 1));
            }
        }

    }


}
