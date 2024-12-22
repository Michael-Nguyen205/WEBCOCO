package spring.boot.webcococo.services;


import spring.boot.webcococo.models.requests.AddAuthorForUserRequest;
import spring.boot.webcococo.models.requests.CreatePermissionRequest;
import spring.boot.webcococo.models.requests.RemoveAuthorForUserRequest;
import spring.boot.webcococo.models.response.AuthorizeResponse;
import spring.boot.webcococo.models.response.UserPermissionResponse;

public interface IAuthorizeService {

    AuthorizeResponse createAuthor(CreatePermissionRequest permissionRequest);

    UserPermissionResponse addAuthor(AddAuthorForUserRequest permissionRequest);

    AuthorizeResponse updateAuthor(CreatePermissionRequest permissionRequest ,Integer Id);

    UserPermissionResponse removeAuthorForUser(RemoveAuthorForUserRequest permissionRequest);

}
