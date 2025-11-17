
package com.example.demo.Config;

import java.util.List;


public class WebhookRequest {
    private String object;
    private List<Entry> entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    // Getters, Setters
    public static class Entry {
        private String id;
        private List<Change> changes;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public List<Change> getChanges() {
            return changes;
        }
        public void setChanges(List<Change> changes) {
            this.changes = changes;
        }

        // Getters, Setters
    }

    public static class Change {
        private String field;
        private Value value;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Value getValue() {
            return value;
        }

        public void setValue(Value value) {
            this.value = value;
        }

        // Getters, Setters

    }

    public static class Value {
        private String messaging_product;
        private Metadata metadata;
        private List<Message> messages;
        private List<Status> statuses;

        public String getMessaging_product() {
            return messaging_product;
        }

        public void setMessaging_product(String messaging_product) {
            this.messaging_product = messaging_product;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        public List<Status> getStatuses() {
            return statuses;
        }

        public void setStatuses(List<Status> statuses) {
            this.statuses = statuses;
        }

        // Getters, Setters
    }

    public static class Metadata {
        private String display_phone_number;
        private String phone_number_id;
        public String getDisplay_phone_number() {
            return display_phone_number;
        }
        public void setDisplay_phone_number(String display_phone_number) {
            this.display_phone_number = display_phone_number;
        }
        public String getPhone_number_id() {
            return phone_number_id;
        }
        public void setPhone_number_id(String phone_number_id) {
            this.phone_number_id = phone_number_id;
        }

        // Getters, Setters
    }

    public static class Message {
        private String from;
        private String id;
        private String timestamp;
        private Text text;
        private String type;
        public String getFrom() {
            return from;
        }
        public void setFrom(String from) {
            this.from = from;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public Text getText() {
            return text;
        }
        public void setText(Text text) {
            this.text = text;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        // Getters, Setters
    }

    public static class Text {
        private String body;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        // Getters, Setters
    }

    public static class Status {
        private String id;
        private String status;
        private String timestamp;
        private String recipient_id;
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getRecipient_id() {
            return recipient_id;
        }
        public void setRecipient_id(String recipient_id) {
            this.recipient_id = recipient_id;
        }

        // Getters, Setters

    }

}
