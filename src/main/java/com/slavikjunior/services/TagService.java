package com.slavikjunior.services;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.Tag;
import com.slavikjunior.models.TransactionTag;
import com.slavikjunior.util.AppLogger;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public List<Tag> getTagsForTransaction(int transactionId, String transactionType) {
        try {
            List<TransactionTag> transactionTags = EntityManager.INSTANCE.get(
                    TransactionTag.class,
                    Map.of("transaction_id", transactionId, "transaction_type", transactionType)
            );

            if (transactionTags == null || transactionTags.isEmpty()) {
                return Collections.emptyList();
            }

            List<Tag> tags = new ArrayList<>();
            for (TransactionTag tt : transactionTags) {
                try {
                    List<Tag> foundTags = EntityManager.INSTANCE.get(Tag.class, Map.of("id", tt.getTagId()));
                    if (foundTags != null && !foundTags.isEmpty()) {
                        tags.add(foundTags.get(0));
                    }
                } catch (Exception e) {
                    log.warning("Error loading tag with id " + tt.getTagId() + ": " + e.getMessage());
                }
            }
            return tags;
        } catch (Exception e) {
            log.severe("Error loading transaction tags: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> getTagNamesForTransaction(int transactionId, String transactionType) {
        List<Tag> tags = getTagsForTransaction(transactionId, transactionType);
        return tags.stream().map(Tag::getName).collect(Collectors.toList());
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

    public boolean addTagToTransaction(int transactionId, String transactionType, int tagId) {
        try {
            TransactionTag transactionTag = new TransactionTag(0, transactionId, tagId, transactionType.toUpperCase());
            EntityManager.INSTANCE.create(transactionTag);
            log.info("Tag " + tagId + " added to transaction " + transactionId);
            return true;
        } catch (Exception e) {
            log.severe("Error adding tag to transaction: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTagFromTransaction(int transactionId, String transactionType, int tagId) {
        try {
            List<TransactionTag> transactionTags = EntityManager.INSTANCE.get(
                    TransactionTag.class,
                    Map.of("transaction_id", transactionId, "tag_id", tagId, "transaction_type", transactionType.toUpperCase())
            );

            if (transactionTags != null && !transactionTags.isEmpty()) {
                boolean deleted = EntityManager.INSTANCE.delete(TransactionTag.class, transactionTags.get(0).getId());
                if (deleted) {
                    log.info("Tag " + tagId + " removed from transaction " + transactionId);
                }
                return deleted;
            }
            return false;
        } catch (Exception e) {
            log.severe("Error removing tag from transaction: " + e.getMessage());
            return false;
        }
    }

    public void clearTransactionTags(int transactionId, String transactionType) {
        try {
            List<TransactionTag> currentTags = EntityManager.INSTANCE.get(
                    TransactionTag.class,
                    Map.of("transaction_id", transactionId, "transaction_type", transactionType.toUpperCase())
            );

            if (currentTags != null) {
                for (TransactionTag tag : currentTags) {
                    EntityManager.INSTANCE.delete(TransactionTag.class, tag.getId());
                }
                log.info("Cleared all tags for transaction " + transactionId);
            }
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
            if (transactionTags != null) {
                for (TransactionTag tt : transactionTags) {
                    EntityManager.INSTANCE.delete(TransactionTag.class, tt.getId());
                }
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

    public Map<Integer, String> getUserTagsMap(int userId) {
        List<Tag> tags = getUserTags(userId);
        return tags.stream().collect(Collectors.toMap(Tag::getId, Tag::getName));
    }
}