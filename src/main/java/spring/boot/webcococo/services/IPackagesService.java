package spring.boot.webcococo.services;

import spring.boot.webcococo.models.requests.PackagesRequest;
import spring.boot.webcococo.models.requests.PaymentTermRequest;
import spring.boot.webcococo.models.response.PackagesResponse;

import java.sql.SQLException;
import java.util.Set;

public interface IPackagesService {


    PackagesResponse createPackage(PackagesRequest packagesRequest , PaymentTermRequest paymentTermRequest );


    PackagesResponse updatePackage(Integer id ,PackagesRequest packagesRequest , PaymentTermRequest paymentTermRequest );
    void deletePackage(Integer id );

    PackagesResponse getPackage(Integer id );



    Set<PackagesResponse> getAllPackage(Integer categorieId) throws SQLException;


}
