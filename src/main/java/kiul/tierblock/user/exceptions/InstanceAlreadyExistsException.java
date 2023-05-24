package kiul.tierblock.user.exceptions;

public class InstanceAlreadyExistsException extends RuntimeException {

    public InstanceAlreadyExistsException(Class<?> clazz) {        
        super(
            String.format("An instance of %s already exists!",
                clazz.getSimpleName())
            );
    }
    
}
