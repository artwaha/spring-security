package orci.or.tz.appointments.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.user.LoginRequest;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@RequestMapping("/api/setup/")
@Api(value = "User Login", description = "Manage Login on the web")
public interface LoginApi {

    @ApiOperation(value = "User Login", notes = "User Login")
    @RequestMapping(value = "/account/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> UserLogin(@RequestBody LoginRequest request) throws IOException, OperationFailedException;

}
