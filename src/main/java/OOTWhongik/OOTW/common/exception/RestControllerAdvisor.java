package OOTWhongik.OOTW.common.exception;

import OOTWhongik.OOTW.clothes.exception.*;
import OOTWhongik.OOTW.member.exception.LocationNotFoundException;
import OOTWhongik.OOTW.outfit.exception.CrawlingException;
import OOTWhongik.OOTW.outfit.exception.InvalidDateException;
import OOTWhongik.OOTW.outfit.exception.OutfitNotFoundException;
import OOTWhongik.OOTW.outfit.exception.UnauthorizedOutfitAccessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class RestControllerAdvisor {
    @ExceptionHandler(UnauthorizedClothesAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedClothesAccessException
            (UnauthorizedClothesAccessException ex, HttpServletRequest request) {
        log.error("UnauthorizedClothesAccessException occurred: {}", ex.getMessage());
        String errorMessage = "옷의 소유주가 아닙니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException
            (CategoryNotFoundException ex, HttpServletRequest request) {
        log.error("CategoryNotFoundException occurred: {}", ex.getMessage());
        String errorMessage = "카테고리를 찾을 수 없습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleSizeLimitExceededException
            (SizeLimitExceededException ex, HttpServletRequest request)  {
        log.error("SizeLimitExceededException occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "사진의 크기가 너무 큽니다.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileUploadException
            (FileUploadException ex, HttpServletRequest request)  {
        log.error("FileUploadException occurred: {}", ex.getMessage());
        String errorMessage = "파일 업로드에 실패했습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClothesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClothesNotFoundException
            (ClothesNotFoundException ex, HttpServletRequest request)  {
        log.error("ClothesNotFoundException occurred: {}", ex.getMessage());
        String errorMessage = "옷을 찾을 수 없습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClothesInUseException.class)
    public ResponseEntity<ErrorResponse> handleClothesInUseException
            (ClothesInUseException ex, HttpServletRequest request)  {
        log.error("ClothesInUseException occurred: {}", ex.getMessage());
        String errorMessage = "옷이 쓰이고 있는 착장이 있습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLocationNotFoundException
            (LocationNotFoundException ex, HttpServletRequest request)  {
        log.error("LocationNotFoundException occurred: {}", ex.getMessage());
        String errorMessage = "지역을 찾을 수 없습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDateException
            (InvalidDateException ex, HttpServletRequest request)  {
        log.error("InvalidDateException occurred: {}", ex.getMessage());
        String errorMessage = "잘못된 날짜입니다. (날짜가 미래이거나 잘못된 형식입니다.)";
        return handleRuntimeException(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OutfitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOutfitNotFoundException
            (OutfitNotFoundException ex, HttpServletRequest request)  {
        log.error("OutfitNotFoundException occurred: {}", ex.getMessage());
        String errorMessage = "착장을 찾을 수 없습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedOutfitAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedOutfitAccessException
            (UnauthorizedOutfitAccessException ex, HttpServletRequest request)  {
        log.error("UnauthorizedOutfitAccessException occurred: {}", ex.getMessage());
        String errorMessage = "착장의 소유주가 아닙니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CrawlingException.class)
    public ResponseEntity<ErrorResponse> handleCrawlingException
            (CrawlingException ex, HttpServletRequest request)  {
        log.error("CrawlingException occurred: {}", ex.getMessage());
        String errorMessage = "날씨 정보를 가져오는데 실패했습니다.";
        return handleRuntimeException(errorMessage, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException
            (MethodArgumentNotValidException ex, HttpServletRequest request)  {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());

        String message;
        if (ex.getBindingResult().getFieldErrors().isEmpty()) {
            message = ex.getMessage();
        } else {
            message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ErrorResponse> handleRuntimeException
            (String errorMessage, HttpServletRequest request, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status,
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
