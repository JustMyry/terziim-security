package com.terziim.backend.icpcommication.response;


import com.terziim.backend.exception.StatusCode;
import com.terziim.backend.exception.StatusMessage;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IcpResponseStatus {

    private Integer code;
    private String message;

    public static IcpResponseStatus getSuccess(){
        return IcpResponseStatus.builder()
                .code(StatusCode.SUCCESS)
                .message(StatusMessage.SUCCESS)
                .build();
    }

    public static IcpResponseStatus getRequestIsInvalid(){
        return IcpResponseStatus.builder()
                .code(StatusCode.REQUEST_IS_INVALID)
                .message(StatusMessage.REQUEST_IS_INVALID)
                .build();
    }


    public static IcpResponseStatus getJwtIsInvalid(){
        return IcpResponseStatus.builder()
                .code(StatusCode.JWT_IS_NOT_VALID)
                .message(StatusMessage.JWT_IS_NOT_VALID)
                .build();
    }


    public static IcpResponseStatus getPassIsWrong(){
        return IcpResponseStatus.builder()
                .code(StatusCode.PASSWORD_IS_WRONG)
                .message(StatusMessage.PASSWORD_IS_WRONG)
                .build();
    }
}
