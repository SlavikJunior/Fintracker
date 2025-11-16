package com.slavikjunior.services;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.Tag;
import com.slavikjunior.models.TransactionTag;
import com.slavikjunior.util.AppLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TagService {
    private static final Logger log = AppLogger.get(TagService.class);

    public List<Tag> getUserTags(int userId) {
        try {
            List<Tag> tags = EntityManager.INSTANCE.get(Tag.class, Map.of("user_id", userId));
            return tags != null ? tags : Collections.emptyList();
        } catch (Exception e) {
            log.severe("Error loading user tags: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Tag> getTagsForTransaction(int transactionId) {
        try {
            List<TransactionTag> transactionTags = EntityManager.INSTANCE.get(
                    TransactionTag.class,
                    Map.of("transaction_id", transactionId)
            );

            if (transactionTags == null || transactionTags.isEmpty())
                return Collections.emptyList();

            List<Tag> tags = new ArrayList<>();
            for (TransactionTag tt : transactionTags) {
                Tag tag = EntityManager.INSTANCE.getUnique(Tag.class, Map.of("id", tt.getTagId()));
                if (tag != null)
                    tags.add(tag);
            }
            return tags;
        } catch (Exception e) {
            log.severe("Error loading transaction tags: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean isTagExists(String name, int userId) {
        try {
            List<Tag> existingTags = EntityManager.INSTANCE.get(Tag.class, Map.of("name", name, "user_id", userId));
            return existingTags != null && !existingTags.isEmpty();
        } catch (Exception e) {
            log.severe("Error checking tag existence: " + e.getMessage());
            return false;
        }
    }

    public void addTagToTransaction(int transactionId, int tagId) {
        try {
            TransactionTag transactionTag = new TransactionTag(0, transactionId, tagId);
            EntityManager.INSTANCE.create(transactionTag);
            log.info("Tag " + tagId + " added to transaction " + transactionId);
        } catch (Exception e) {
            log.severe("Error adding tag to transaction: " + e.getMessage());
        }
    }

    public void clearTransactionTags(int transactionId) {
        try {
            List<TransactionTag> currentTags = EntityManager.INSTANCE.get(
                    TransactionTag.class,
                    Map.of("transaction_id", transactionId)
            );

            if (currentTags == null || currentTags.isEmpty())
                return;

            for (TransactionTag tag : currentTags) {
                EntityManager.INSTANCE.delete(TransactionTag.class, tag.getId());
            }
            log.info("Cleared all tags for transaction " + transactionId);
        } catch (Exception e) {
            log.severe("Error clearing transaction tags: " + e.getMessage());
        }
    }

    public Tag createTag(String name, int userId, String color) {
        try {
            if (isTagExists(name, userId)) {
                log.warning("Tag already exists: " + name + " for user " + userId);
                return null;
            }
            return EntityManager.INSTANCE.create(new Tag(0, name, userId, color));
        } catch (Exception e) {
            log.severe("Error creating tag: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteTag(int tagId) {
        try {
            List<TransactionTag> transactionTags = EntityManager.INSTANCE.get(TransactionTag.class, Map.of("tag_id", tagId));

            if (transactionTags == null || transactionTags.isEmpty())
                return false;

            for (TransactionTag tt : transactionTags) {
                EntityManager.INSTANCE.delete(TransactionTag.class, tt.getId());
            }
            boolean deleted = EntityManager.INSTANCE.delete(Tag.class, tagId);
            if (deleted) {
                log.info("Deleted tag " + tagId);
            }
            return deleted;
        } catch (Exception e) {
            log.severe("Error deleting tag: " + e.getMessage());
            return false;
        }
    }
}