package com.example.demo.DB;

/**
 * Represents the different types of messages that can be sent or received.
 * <p>
 * The available message types are:
 * <ul>
 *     <li>{@link #TEXT} - A text message.</li>
 *     <li>{@link #IMAGE} - An image message.</li>
 *     <li>{@link #DOCUMENT} - A document message.</li>
 *     <li>{@link #TEMPLATE} - A template message.</li>
 *     <li>{@link #INTERACTIVE} - An interactive message.</li>
 * </ul>
 * </p>
 */
public enum MessageType {
    TEXT, IMAGE, DOCUMENT, TEMPLATE, INTERACTIVE
}