package ru.mirea.oop.practice.coursej.s131226;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.api.VkontakteApi;
import ru.mirea.oop.practice.coursej.api.vk.DocumentsApi;
import ru.mirea.oop.practice.coursej.api.vk.MessagesApi;
import ru.mirea.oop.practice.coursej.api.vk.entities.Document;

import java.io.File;
import java.io.IOException;

public class OutgoingMessage {
    private static final Logger logger = LoggerFactory.getLogger(OutgoingMessage.class);
    private long id;
    private String text;
    private String attachment;
    private final VkontakteApi api;
    public static final int CHECK_COUNT = 10;


    public OutgoingMessage(long id, VkontakteApi api) {
        this.id = id;
        this.api = api;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public void setAttachment(File file) {
        DocumentsApi documentsApi = null;
        try {
            documentsApi = api.getDocuments();

            Document[] documents = documentsApi.list(CHECK_COUNT, 0, api.idOwner());
            for (Document existDoc : documents) {
                if (existDoc.title.equals(file.getName())) {
                    if (documentsApi.delete(existDoc) == 1) logger.debug("успешно удален " + existDoc.title);
                }
            }

            documentsApi.uploadDocument(file);
            documents = documentsApi.list(1, 0, api.idOwner());
            this.setAttachment("doc" + documents[0].idOwner + "_" + documents[0].id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getText() {
        return text;
    }

    public String getAttachment() {
        return attachment;
    }

    public int send(MessagesApi api) {
        try {
            return api.send(id, null, null, null, text, null, null, null, attachment, null, null);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return 0;
    }

}
