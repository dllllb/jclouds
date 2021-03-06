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
package org.jclouds.gogrid.predicates;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import org.jclouds.gogrid.domain.Job;
import org.jclouds.gogrid.domain.JobState;
import org.jclouds.gogrid.domain.LoadBalancer;
import org.jclouds.gogrid.options.GetJobListOptions;
import org.jclouds.gogrid.services.GridJobClient;
import org.jclouds.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Oleksiy Yarmula
 */
@Singleton
public class LoadBalancerLatestJobCompleted implements Predicate<LoadBalancer> {

    protected GridJobClient jobClient;

    @Resource
    protected Logger logger = Logger.NULL;

    @Inject
    public LoadBalancerLatestJobCompleted(GridJobClient jobClient) {
        this.jobClient = jobClient;
    }

    @Override
    public boolean apply(LoadBalancer loadBalancer) {
        checkNotNull(loadBalancer, "Load balancer must be a valid instance");
        checkNotNull(loadBalancer.getName(), "Load balancer must be a valid name");
        GetJobListOptions jobOptions = new GetJobListOptions.Builder().
                latestJobForObjectByName(loadBalancer.getName());
        Job latestJob = Iterables.getOnlyElement(jobClient.getJobList(jobOptions));
        return JobState.SUCCEEDED.equals(latestJob.getCurrentState());
    }
}
