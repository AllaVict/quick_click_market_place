package quick.click.security.commons.utils;

import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Base64;
import java.util.Optional;

/**
 * A utility class providing static methods for handling cookies, including retrieval, addition,
 * deletion, serialization, and deserialization of objects to and from cookies.
 *
 * @author Alla Borodina
 */
public class CookieUtils {

    /**
     * Retrieves a cookie from the request by its name.
     *
     * @param request the HttpServletRequest from which to retrieve the cookie.
     * @param name    the name of the cookie to retrieve.
     * @return an Optional containing the Cookie if found, or an empty Optional if not.
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Adds a cookie to the response.
     *
     * @param response the HttpServletResponse to which the cookie will be added.
     * @param name     the name of the cookie.
     * @param value    the value of the cookie.
     * @param maxAge   the maximum age of the cookie in seconds.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Deletes a cookie from the request and the response.
     *
     * @param request  the HttpServletRequest from which the cookie will be deleted.
     * @param response the HttpServletResponse from which the cookie will be deleted.
     * @param name     the name of the cookie to delete.
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * Serializes an object into a Base64 encoded string.
     *
     * @param object the object to serialize.
     * @return a Base64 encoded string representing the serialized object.
     */
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    /**
     * Deserializes a Base64 encoded string into an object of the specified class.
     *
     * @param cookie the cookie from which the object is to be deserialized.
     * @param cls    the class of the object to be deserialized into.
     * @return an object of type T that is deserialized from the cookie value.
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }

}
