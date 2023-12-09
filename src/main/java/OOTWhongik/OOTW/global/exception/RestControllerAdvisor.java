package OOTWhongik.OOTW.global.exception;

import OOTWhongik.OOTW.domain.clothes.exception.*;
import OOTWhongik.OOTW.domain.member.exception.LocationNotFoundException;
import OOTWhongik.OOTW.domain.outfit.exception.InvalidDateException;
import OOTWhongik.OOTW.domain.outfit.exception.OutfitNotFoundException;
import OOTWhongik.OOTW.domain.outfit.exception.UnauthorizedOutfitAccessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestControllerAdvisor {

    @ExceptionHandler(UnauthorizedClothesAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedClothesAccessException() throws JsonProcessingException {
        return responseJsonMessage("옷의 소유주가 아닙니다.");
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCategoryNotFoundException() throws JsonProcessingException {
        return responseJsonMessage("카테고리 입력이 옳지 않습니다.");
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleSizeLimitExceededException() throws JsonProcessingException {
        return responseJsonMessage("사진의 크기가 너무 큽니다.");
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleFileUploadException() throws JsonProcessingException {
        return responseJsonMessage("파일 업로드에 실패했습니다.");
    }

    @ExceptionHandler(ClothesNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleClothesNotFoundException() throws JsonProcessingException {
        return responseJsonMessage("옷을 찾을 수 없습니다.");
    }

    @ExceptionHandler(ClothesInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleClothesInUseException() throws JsonProcessingException {
        return responseJsonMessage("옷이 쓰이고 있는 착장이 있습니다. 옷이 쓰이고 있는 착장들을 모두 삭제한 후 시도해주십시오.");
    }

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLocationNotFoundException() throws JsonProcessingException {
        return responseJsonMessage("올바르지 않은 지역명을 입력했습니다.");
    }

    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidDateException() throws JsonProcessingException {
        return responseJsonMessage("잘못된 형식의 날짜를 입력했습니다.");
    }

    @ExceptionHandler(OutfitNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleOutfitNotFoundException() throws JsonProcessingException {
        return responseJsonMessage("착장을 찾을 수 없습니다.");
    }

    @ExceptionHandler(UnauthorizedOutfitAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedOutfitAccessException() throws JsonProcessingException {
        return responseJsonMessage("착장의 소유주가 아닙니다.");
    }

    @ExceptionHandler(CrawlingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleCrawlingException() throws JsonProcessingException {
        return responseJsonMessage("날씨 정보를 가져오는데 실패했습니다.");
    }

    private String responseJsonMessage(String message) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return new ObjectMapper().writeValueAsString(map);
    }
}
