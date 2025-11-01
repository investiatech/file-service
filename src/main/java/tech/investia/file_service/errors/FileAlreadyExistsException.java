package tech.investia.file_service.errors;

public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException(String name) {
        super("Object already exists: " + name);
    }
}