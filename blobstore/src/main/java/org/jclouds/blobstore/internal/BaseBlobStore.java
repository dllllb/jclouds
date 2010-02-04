/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
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
package org.jclouds.blobstore.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.blobstore.options.ListContainerOptions.Builder.recursive;

import javax.inject.Inject;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.ContainerNotFoundException;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.util.BlobStoreUtils;
import org.jclouds.blobstore.util.internal.BlobStoreUtilsImpl;
import org.jclouds.util.Utils;

import com.google.common.base.Supplier;

/**
 * 
 * @author Adrian Cole
 */
public abstract class BaseBlobStore implements BlobStore {

   protected final BlobStoreUtils blobUtils;

   @Inject
   protected BaseBlobStore(BlobStoreUtils blobUtils) {
      this.blobUtils = checkNotNull(blobUtils, "blobUtils");
   }

   /**
    * invokes {@link BlobStoreUtilsImpl#newBlob }
    */
   @Override
   public Blob newBlob(String name) {
      return blobUtils.newBlob(name);
   }

   /**
    * This implementation invokes
    * {@link #list(String,org.jclouds.blobstore.options.ListContainerOptions)}
    * 
    * @param container
    *           container name
    */
   @Override
   public PageSet<? extends StorageMetadata> list(String container) {
      return this.list(container, org.jclouds.blobstore.options.ListContainerOptions.NONE);
   }

   /**
    * This implementation invokes {@link BlobStoreUtilsImpl#directoryExists}
    * 
    * @param container
    *           container name
    * @param directory
    *           virtual path
    */
   @Override
   public boolean directoryExists(String containerName, String directory) {
      return blobUtils.directoryExists(containerName, directory);
   }

   /**
    * This implementation invokes {@link BlobStoreUtilsImpl#createDirectory}
    * 
    * @param container
    *           container name
    * @param directory
    *           virtual path
    */
   @Override
   public void createDirectory(String containerName, String directory) {
      blobUtils.createDirectory(containerName, directory);
   }

   /**
    * This implementation invokes {@link #countBlobs} with the
    * {@link ListContainerOptions#recursive} option.
    * 
    * @param container
    *           container name
    */
   @Override
   public long countBlobs(String container) {
      return countBlobs(container, recursive());
   }

   /**
    * This implementation invokes {@link BlobStoreUtilsImpl#countBlobs}
    * 
    * @param container
    *           container name
    */
   @Override
   public long countBlobs(String containerName, ListContainerOptions options) {
      return blobUtils.countBlobs(containerName, options);
   }

   /**
    * This implementation invokes {@link #clearContainer} with the
    * {@link ListContainerOptions#recursive} option.
    * 
    * @param container
    *           container name
    */
   @Override
   public void clearContainer(String containerName) {
      clearContainer(containerName, recursive());
   }

   /**
    * This implementation invokes {@link BlobStoreUtilsImpl#clearContainer}
    * 
    * @param container
    *           container name
    */
   @Override
   public void clearContainer(String containerName, ListContainerOptions options) {
      blobUtils.clearContainer(containerName, options);
   }

   /**
    * This implementation invokes {@link BlobStoreUtilsImpl#deleteDirectory}.
    * 
    * @param container
    *           container name
    */
   @Override
   public void deleteDirectory(String containerName, String directory) {
      blobUtils.deleteDirectory(containerName, directory);
   }

   /**
    * This implementation invokes
    * {@link #getBlob(String,String,org.jclouds.blobstore.options.GetOptions)}
    * 
    * @param container
    *           container name
    * @param key
    *           blob key
    */
   @Override
   public Blob getBlob(String container, String key) {
      return getBlob(container, key, org.jclouds.blobstore.options.GetOptions.NONE);
   }

   /**
    * This implementation invokes {@link #deleteAndEnsurePathGone}
    * 
    * @param container
    *           bucket name
    */
   @Override
   public void deleteContainer(final String container) {
      clearAndDeleteContainer(container);
   }

   protected void clearAndDeleteContainer(final String container) {
      try {
         if (!Utils.enventuallyTrue(new Supplier<Boolean>() {
            public Boolean get() {
               try {
                  clearContainer(container, recursive());
                  return deleteAndVerifyContainerGone(container);
               } catch (ContainerNotFoundException e) {
                  return true;
               }
            }

         }, 30000)) {
            throw new IllegalStateException(container + " still exists after deleting!");
         }
      } catch (InterruptedException e) {
         new IllegalStateException(container + " interrupted during deletion!", e);
      }
   }

   protected abstract boolean deleteAndVerifyContainerGone(String container);
}