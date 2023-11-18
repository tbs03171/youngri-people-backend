package hello.movie;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomResponse {
    private String message;
    private Object data;

    @Builder
    public CustomResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    @Builder
    public CustomResponse(String message){
        this.message = message;
    }
}
