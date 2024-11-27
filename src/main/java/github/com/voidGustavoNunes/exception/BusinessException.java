package github.com.voidGustavoNunes.exception;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BusinessException(@NotNull @Positive Long id, String message) {
        super("=== Erro ao processar a entidade com o id: " + id + "\n" + "Mensagem: " + message    +   " ===\n");
    }
}
