package com.ados.xbook.helper;

import com.ados.xbook.domain.entity.SessionEntity;
import com.ados.xbook.exception.InvalidException;
import com.ados.xbook.repository.SessionEntityRepo;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
public class StringHelper {

    public static String toSlug(String input) {
        return input.toLowerCase().replaceAll("[á|à|ả|ạ|ã|ă|ắ|ằ|ẳ|ẵ|ặ|â|ấ|ầ|ẩ|ẫ|ậ|ä|å|æ|ą]", "a")
                .replaceAll("[ó|ò|ỏ|õ|ọ|ô|ố|ồ|ổ|ỗ|ộ|ơ|ớ|ờ|ở|ỡ|ợ|ö|ô|œ|ø]", "o")
                .replaceAll("[é|è|ẻ|ẽ|ẹ|ê|ế|ề|ể|ễ|ệ|ě|ė|ë|ę]", "e").replaceAll("[ú|ù|ủ|ũ|ụ|ư|ứ|ừ|ử|ữ|ự]", "u")
                .replaceAll("[i|í|ì|ỉ|ĩ|ị|ï|î|į]", "i").replaceAll("[ù|ú|ü|û|ǘ|ů|ű|ū|ų]", "u")
                .replaceAll("[ß|ş|ś|š|ș]", "s").replaceAll("[ź|ž|ż]", "z").replaceAll("[ý|ỳ|ỷ|ỹ|ỵ|ÿ|ý]", "y")
                .replaceAll("[ǹ|ń|ň|ñ]", "n").replaceAll("[ç|ć|č]", "c").replaceAll("[ğ|ǵ]", "g")
                .replaceAll("[ŕ|ř]", "r").replaceAll("[·|/|_|,|:|;]", "-").replaceAll("[ť|ț]", "t").replaceAll("ḧ", "h")
                .replaceAll("ẍ", "x").replaceAll("ẃ", "w").replaceAll("ḿ", "m").replaceAll("ṕ", "p")
                .replaceAll("ł", "l").replaceAll("đ", "d").replaceAll("\\s+", "-").replaceAll("&", "-and-")
                .replaceAll("[^\\w\\-]+", "").replaceAll("\\-\\-+", "-").replaceAll("^-+", "").replaceAll("-+$", "");
    }

    public static SessionEntity info(String token, SessionEntityRepo repo) {

        if (Strings.isNullOrEmpty(token)) {
            throw new InvalidException("Token is null or empty");
        }

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Optional<SessionEntity> optional = repo.findById(token);

        if (!optional.isPresent()) {
            log.info("Can't find session by token: {}", token);
            throw new InvalidException("Can't find session by token: " + token);
        }

        SessionEntity session = optional.get();

        return session;

    }

}
