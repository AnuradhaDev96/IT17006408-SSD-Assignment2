package com.billboard.IT17006408_oAuth.Controller;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.drive;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.security.Principal;
import java.util.Arrays;

@org.springframework.stereotype.Controller
public class Controller {

    private Principal principal;
    private TokenResponse tokenResponse = new TokenResponse();
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @GetMapping("/")
    public String helloWorld(){
        return "index";
    }

    @GetMapping("/restricted")
    public String restricted(){
        return "when logged in";
    }

//    @RequestMapping(value = "/user")
//    public Principal user(Principal principal) throws Exception{
//
////        System.out.println(user);
////        String s = properties.get("scope").toString();
////        System.out.println(s);
////        String s = principal.getName();
//        return principal;
//    }

    @GetMapping(value = "/user")
    public String user(@AuthenticationPrincipal OidcUser principal) throws Exception{

//        System.out.println(user);
//        String s = properties.get("scope").toString();
//        System.out.println(s);
        tokenResponse.setAccessToken(principal.getAccessTokenHash());
        tokenResponse.setExpiresInSeconds((long) 3600);
        tokenResponse.setTokenType("bearer");

        System.out.println(principal);
        System.out.println(tokenResponse);
//        String s = principal.get();
        return "uploadFile";
    }
    //http://localhost:8080/login/oauth2/code/google

    @RequestMapping(value = "/createFile")
    public void createFile(HttpServletResponse response) throws Exception{
        Credential credential = createCredentialWithAccessTokenOnly(HTTP_TRANSPORT, JSON_FACTORY, tokenResponse);
        Drive gdrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("BillboardOAuth").build();

        com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName("profile.jpg");

        FileContent content = new FileContent("image/jpeg", new File("H:\\Internship\\Springboot\\gdrive\\cat.jpg"));
        com.google.api.services.drive.model.File uploadFile = gdrive.files().create(file, content).setFields("id").execute();
//        credential.setAccessToken()
        String fileReference = String.format("{fileID: '%s'}", uploadFile.getId());
        response.getWriter().write(fileReference);

        System.out.println("HI............");
        System.out.println(tokenResponse);
//        return principal;
    }

    @GetMapping(value = {"/createFolder/{folderName}"}, produces = "application/json")
    public @ResponseBody Message createFolder(@PathVariable(name = "folderName") String folder) throws Exception{
        Credential credential = createCredentialWithAccessTokenOnly(HTTP_TRANSPORT, JSON_FACTORY, tokenResponse);
        Drive gdrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("BillboardOAuth").build();

        com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName(folder);
        file.setMimeType("application/vnd.google-apps.folder");

        gdrive.files().create(file).execute();

        Message message = new Message();
        message.setMessage("Folder created successfully");
        return message;
    }

    @GetMapping(value = {"/uploadInFolder"})
    public void uploadFileInFolder(HttpServletResponse response) throws Exception{
        Credential credential = createCredentialWithAccessTokenOnly(HTTP_TRANSPORT, JSON_FACTORY, tokenResponse);
        Drive gdrive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("BillboardOAuth").build();

        com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName("cat.jpg");
        file.setParents(Arrays.asList(""));

        FileContent content = new FileContent("image/jpeg", new File("H:\\Internship\\Springboot\\gdrive\\cat.jpg"));
        com.google.api.services.drive.model.File uploadFile = gdrive.files().create(file, content).setFields("id").execute();

        String fileReference = String.format("{fileID: '%s'}", uploadFile.getId());
        response.getWriter().write(fileReference);

    }

    public static Credential createCredentialWithAccessTokenOnly(HttpTransport transport, JsonFactory jsonFactory, TokenResponse tokenResponse) {
        return new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(tokenResponse);
    }

}
