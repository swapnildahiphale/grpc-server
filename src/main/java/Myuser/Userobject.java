package Myuser;

import com.pmm.User;
import com.pmm.userGrpc;
import io.grpc.stub.StreamObserver;
import org.omg.PortableInterceptor.ServerRequestInfo;

public class Userobject extends userGrpc.userImplBase {
    @Override
    public void login(User.loginrequest request, StreamObserver<User.Apiresponse> responseObserver) {
        String username= request.getUsername();
        String pass = request.getPassword();
        User.Apiresponse.Builder test = User.Apiresponse.newBuilder();
        System.out.println("Serving request for user: " + username);
        if (username.equals(pass))
        {
        //   test.setResposebody("this is success response, login successful");
          test.setResposebody("Login sussessful for user : " + username);
          test.setResposecode("200");

        }
        else
        {
            test.setResposebody("login failure");
            test.setResposecode("401");
        }
        responseObserver.onNext(test.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(User.userrequest request, StreamObserver<User.Apiresponse> responseObserver) {
        String username= request.getUsername();
        User.Apiresponse.Builder test = User.Apiresponse.newBuilder();
        test.setResposebody("Hi, "+username);
        test.setResposecode("200");
        responseObserver.onNext(test.build());
        responseObserver.onCompleted();

    }

    @Override
    public void getMyName(User.Empty request, StreamObserver<User.Apiresponse> responseObserver) {

        User.Apiresponse.Builder test = User.Apiresponse.newBuilder();
        test.setResposebody("Hi, MPl container team");
        test.setResposecode("200");
        responseObserver.onNext(test.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.Empty request, StreamObserver<User.Apiresponse> responseObserver) {
        super.logout(request, responseObserver);
    }
}
