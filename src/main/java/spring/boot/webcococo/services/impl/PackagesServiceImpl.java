package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.Categories;
import spring.boot.webcococo.entities.Packages;
import spring.boot.webcococo.entities.PackagesFeature;
import spring.boot.webcococo.entities.PaymentTerms;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.mapping.PackagepProjectionMapping;
import spring.boot.webcococo.models.requests.PackageFeatureRequest;
import spring.boot.webcococo.models.requests.PackagesRequest;
import spring.boot.webcococo.models.requests.PaymentTermRequest;
import spring.boot.webcococo.models.response.*;
import spring.boot.webcococo.repositories.CategoryRepository;
import spring.boot.webcococo.repositories.PackagesFeatureRepository;
import spring.boot.webcococo.repositories.PackagesRepository;
import spring.boot.webcococo.repositories.PaymentTermRepository;
import spring.boot.webcococo.services.IPackagesService;
import spring.boot.webcococo.utils.EntityCascadeDeletionUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PackagesServiceImpl extends BaseServiceImpl<Packages, Integer, PackagesRepository> implements IPackagesService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;

    private final PackagesRepository packagesRepository;

    private final PackagesFeatureRepository packagesFeatureRepository;

    private final PaymentTermServiceImpl paymentTermService;
    private final PaymentTermRepository paymentTermRepository;

    private final CategoryRepository categoryRepository;

    private final EntityCascadeDeletionUtil deletionUtil;

    private final JdbcTemplate jdbcTemplate;
    AtomicInteger loopCounter = new AtomicInteger(0);
    AtomicInteger loopCreateOpp = new AtomicInteger(0);
    AtomicInteger loopCreateOppPackageFeature = new AtomicInteger(0);
    AtomicInteger loopCreateOppPaymentConditionOnMonthly = new AtomicInteger(0);
    AtomicInteger loopCreateOppPaymentMethods = new AtomicInteger(0);
    AtomicInteger loopCreateOppBankGateway = new AtomicInteger(0);
    //    private Set<PackagesFeatureResponse> mapPackagesFeatures(Set<PackagepProjectionMapping> packagepProjectionMappings) throws SQLException {
//
//        // Sử dụng Stream để xử lý dữ liệu thay vì TreeSet
//        return packagepProjectionMappings.stream()
//                .distinct() // Loại bỏ các phần tử trùng lặp (nếu cần)
//                .sorted(Comparator
//                        .comparingInt(PackagepProjectionMapping::getPackageFeatureId)
//                        .thenComparingInt(PackagepProjectionMapping::getPackageFeaturePackageId)) // Sắp xếp
//                .peek(item -> loopCounter.incrementAndGet()) // Tăng counter mỗi lần duyệt
//                .map(packagepProjectionMapping -> {
//                    loopCreateOppPackageFeature.incrementAndGet();
//                    loopCreateOpp.incrementAndGet();
//
//                    // Chuyển đổi PackagepProjectionMapping thành PackagesFeatureResponse
//                    return PackagesFeatureResponse.builder()
//                            .id(packagepProjectionMapping.getPackageFeatureId())
//                            .feature(packagepProjectionMapping.getPackageFeatureName())
//                            .packageId(packagepProjectionMapping.getPackageFeaturePackageId())
//                            .build();
//                })
//                .collect(Collectors.toSet());
//    }

    public PackagesServiceImpl(PackagesRepository repository, PackagesRepository packagesRepository, PackagesFeatureRepository packagesFeatureRepository, PaymentTermServiceImpl paymentTermService, PaymentTermRepository paymentTermRepository,
//                               PaymentTermServiceImpl paymentTermService,
                               CategoryRepository categoryRepository, EntityCascadeDeletionUtil deletionUtil, JdbcTemplate jdbcTemplate) {
        super(repository); // Truyền repository vào lớp cha
        this.packagesRepository = packagesRepository;
        this.packagesFeatureRepository = packagesFeatureRepository;
        this.paymentTermService = paymentTermService;
        this.paymentTermRepository = paymentTermRepository;
        this.categoryRepository = categoryRepository;
        this.deletionUtil = deletionUtil;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public PackagesResponse createPackage(PackagesRequest
                                                  packagesRequest, PaymentTermRequest paymentTermRequest) {
        log.error("đa vao service");


        // tạo package
        Packages packages = new Packages();
        packages.setName(packagesRequest.getName());
        packages.setDescription(packagesRequest.getDescription());
        Categories categories = categoryRepository.findById(packagesRequest.getCategoriesId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        packages.setCategoriesId(categories.getId());
        packages.setDepositPercent(packagesRequest.getDepositPercent() != null ? packagesRequest.getDepositPercent() : 0);
        try {
            packages = save(packages);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        //---------------------------

        // tạo các packagesFeature
        Set<spring.boot.webcococo.entities.PackagesFeature> packagesFeatureList = new HashSet<>();
        for (PackageFeatureRequest packagesFeatureRequest : packagesRequest.getPackagesFeatureRequestList()) {
            PackagesFeature packagesFeature = new PackagesFeature();
            packagesFeature.setFeature(packagesFeatureRequest.getFeature());
            packagesFeature.setPackageId(packages.getId());
            packagesFeatureList.add(packagesFeature);
        }
        try {
            packagesFeatureRepository.saveAll(packagesFeatureList);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


        //----------------------------------


        //createPaymentTerm--------------------------------------
        PaymentTermResponse paymentTermResponse;
        try {
            paymentTermResponse = paymentTermService.createPaymentTerm(paymentTermRequest, packages.getId(), null);
        } catch (Exception e) {
            throw e;
        }

        return PackagesResponse.toPackagesResponse(packages, packagesFeatureList, paymentTermResponse, null);
    }


    @Override
    public PackagesResponse updatePackage(Integer id, PackagesRequest packagesRequest, PaymentTermRequest paymentTermRequest) {


        // createtpackages-------------------------------------
        Packages packages = findById(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        packages.setName(packagesRequest.getName());
        packages.setDescription(packagesRequest.getDescription());
        Categories categories = categoryRepository.findById(packagesRequest.getCategoriesId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        packages.setCategoriesId(categories.getId());
        packages.setDepositPercent(packagesRequest.getDepositPercent());

        try {
            packages = save(packages);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


        // createtpackagesFeature-------------------------------------
        List<PackagesFeature> existingFeatures = packagesFeatureRepository.getAllByPackageId(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
        packagesFeatureRepository.deleteAll(existingFeatures);
        for (PackageFeatureRequest packagesFeatureRequest : packagesRequest.getPackagesFeatureRequestList()) {
            PackagesFeature packagesFeature = new PackagesFeature();
            packagesFeature.setFeature(packagesFeatureRequest.getFeature());
            packagesFeature.setPackageId(packages.getId());
            existingFeatures.add(packagesFeature);
        }


        try {
            packagesFeatureRepository.saveAll(existingFeatures);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


        //updatePaymentTerm--------------------------------------
        PaymentTermResponse paymentTermResponse;
        try {
            paymentTermResponse = paymentTermService.createPaymentTerm(paymentTermRequest, packages.getId(), null);
        } catch (Exception e) {
            throw e;
        }
        return null;
    }








    public Set<PackagesResponse> getAllPackageByCategorieId(Integer categorieId) throws SQLException {
        String query = """
                  SELECT\s
                                 p.id AS p_id,\s
                                 p.name AS p_name,\s
                                 p.description AS p_description,\s
                                 p.categories_id AS p_categories_id,\s
                                 p.deposit_percent AS p_deposit_percent,\s
                                 ppi.image AS p_image,
                                 pf.id AS pf_id,\s
                                 pf.feature AS pf_name,\s
                                 pt.id AS pt_id,\s
                                 pt.name AS pt_name,
                                 pt.price AS pt_price,\s
                                 pcb.id AS pcb_id,\s
                                 pcb.min_budget AS pcb_min_budget,\s
                                 pcb.max_budget AS pcb_max_budget,\s
                                 pcb.fixed_fee AS pcb_fixed_fee,\s
                                 pcb.percentage_fee AS pcb_percentage_fee,\s
                                 pcm.id AS pcm_id,\s
                                 pcm.price AS pcm_price,\s
                                 pcm.duration_months AS pcm_duration_months
                             FROM\s
                                 packages p
                             LEFT JOIN\s
                                 packages_feature pf ON p.id = pf.package_id
                             LEFT JOIN\s
                                 product_package_images ppi ON p.id =  ppi.package_id
                             LEFT JOIN\s
                                 payment_terms pt ON p.id = pt.package_id
                             LEFT JOIN\s
                                 payment_condition_on_budget pcb ON pt.id = pcb.payment_terms_id
                             LEFT JOIN\s
                                 payment_condition_on_monthly pcm ON pt.id = pcm.payment_terms_id
                             WHERE
                                 p.categories_id = ?;
                """;


        try {
            return jdbcTemplate.query(query, new Object[]{categorieId}, rs -> {

                Set<PackagepProjectionMapping> packagepProjectionMappings = PackagepProjectionMapping.toPackageMapping(rs, loopCounter, loopCreateOpp);

//                Set<Integer> packageIdList = packagepProjectionMappings.stream().map(PackagepProjectionMapping::getPackageId).collect(Collectors.toSet());

                return mapToPackagesResponseList(packagepProjectionMappings);
            });

        } catch (Exception ex) {
            log.error("Lỗi khi thực hiện truy vấn với categorieId: {}", categorieId, ex);
            throw ex;
        }


    }


//    private PackagesResponse mapToPackagesResponsee(Set<PackagepProjectionMapping> packagepProjectionMappings) throws SQLException {
//        // Kiểm tra xem có kết quả hay không
//        PackagepProjectionMapping packagepProjectionMapping = packagepProjectionMappings.iterator().next();
//        return PackagesResponse.builder()
//                .id(packagepProjectionMapping.getPackageId())
//                .name(packagepProjectionMapping.getPackageName())
//                .description(packagepProjectionMapping.getPackageDescription())
//                .categoriesId(packagepProjectionMapping.getPackageCategoriesId())
//                .packagesFeature(mapPackagesFeatures(packagepProjectionMappings))  // Lấy danh sách feature
//                .paymentTerm(mapPaymentTerm(packagepProjectionMappings))  // Lấy payment terms
//                .build();
//    }

    private Set<PackagesResponse> mapToPackagesResponseList(Set<PackagepProjectionMapping> packagepProjectionMappings) throws SQLException {
// Bước 1: Nhóm dữ liệu theo packageId

        Map<Integer, Set<PackagepProjectionMapping>> groupedByPackageId = packagepProjectionMappings.stream()
                .peek(item -> {
                    loopCounter.incrementAndGet();
                })
                .collect(Collectors.groupingBy(PackagepProjectionMapping::getPackageId, Collectors.toSet()));

// Bước 2: Duyệt qua từng nhóm packageId và tạo response
        Set<PackagesResponse> packageResponses = groupedByPackageId.entrySet().stream()
                .peek(item -> {
                    loopCounter.incrementAndGet();
                })
                .map(entry -> {
                    Integer packageId = entry.getKey();  // packageId là key của Map
                    Set<PackagepProjectionMapping> mappings = entry.getValue();  // mappings là value của Map
                    // Dùng mappings để tạo ra PackagesResponse
                    try {
                        return mapToPackagesResponse(mappings);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        log.error("loopCounter:{}", loopCounter);

        log.error("loopCreateOpp:{}", loopCreateOpp);


        log.error("loopCreateOppPackageFeature:{}", loopCreateOppPackageFeature);

        log.error("loopCreateOppPaymentConditionOnMonthly:{}", loopCreateOppPaymentConditionOnMonthly);

        log.error("loopCreateOppPaymentMethods:{}", loopCreateOppPaymentMethods);

        log.error("loopCreateOppBankGateway:{}", loopCreateOppBankGateway);


        return packageResponses;
    }

    public PackagesResponse getPackageById(Integer id) {
        String query = """

                                  \s
                  SELECT\s
                    p.id AS p_id,\s
                    p.name AS p_name,\s
                    p.description AS p_description,\s
                    p.categories_id AS p_categories_id,\s
                    p.deposit AS p_deposit,
                    ppi.image AS p_image,
                    pf.id AS pf_id,\s
                    pf.feature AS pf_name,\s
                    pt.id AS pt_id,\s
                    pt.name AS pt_name,
                    pt.price AS pt_price,\s
                    pcb.id AS pcb_id,\s
                    pcb.min_budget AS pcb_min_budget,\s
                    pcb.max_budget AS pcb_max_budget,\s
                    pcb.fixed_fee AS pcb_fixed_fee,\s
                    pcb.percentage_fee AS pcb_percentage_fee,\s
                    pcm.id AS pcm_id,\s
                    pcm.price AS pcm_price,\s
                    pcm.duration_months AS pcm_duration_months
                                FROM\s
                    packages p
                                LEFT JOIN\s
                    packages_feature pf ON p.id = pf.package_id
                                LEFT JOIN\s
                    product_package_images ppi ON p.id = ppi.package_id
                                LEFT JOIN\s
                    payment_terms pt ON p.id = pt.package_id
                                LEFT JOIN\s
                    payment_condition_on_budget pcb ON pt.id = pcb.payment_terms_id
                                LEFT JOIN\s
                    payment_condition_on_monthly pcm ON pt.id = pcm.payment_terms_id
                                WHERE\s
                    p.id = ?;
                """

       ;
        return jdbcTemplate.query(query, new Object[]{id}, rs -> {
            Set<PackagepProjectionMapping> packagepProjectionMappings = PackagepProjectionMapping.toPackageMapping(rs, loopCounter, loopCreateOpp);
            // Chỉ cần gọi rs.next() một lần

            return mapToPackagesResponse(packagepProjectionMappings);
        });
    }

    private PackagesResponse mapToPackagesResponse(Set<PackagepProjectionMapping> packagepProjectionMappings) throws SQLException {
        // Kiểm tra xem có kết quả hay không

        PackagepProjectionMapping packagepProjectionMapping = packagepProjectionMappings.iterator().next();
        return PackagesResponse.builder()
                .id(packagepProjectionMapping.getPackageId())
                .name(packagepProjectionMapping.getPackageName())
                .description(packagepProjectionMapping.getPackageDescription())
                .categoriesId(packagepProjectionMapping.getPackageCategoriesId())
                .deposit(packagepProjectionMapping.getPackageDepositPercent())
                .packagesFeatureResponses(mapPackagesFeatures(packagepProjectionMappings))  // Lấy danh sách feature
                .packageImagesResponses(mapPackageImages(packagepProjectionMappings))
                .paymentTermResponse(mapPaymentTerm(packagepProjectionMappings))

                // Lấy payment terms
                .build();
    }

    private Set<PackagesFeatureResponse> mapPackagesFeatures(Set<PackagepProjectionMapping> packagepProjectionMappings) {
        // Sử dụng TreeSet với Comparator trực tiếp
        Set<PackagepProjectionMapping> packageFeatureMapping = new TreeSet<>(
                Comparator.comparingInt(PackagepProjectionMapping::getPackageFeatureId)
                        .thenComparingInt(PackagepProjectionMapping::getPackageId)
        );

        // Thêm tất cả phần tử vào TreeSet để loại bỏ các phần tử trùng lặp theo Comparator
        packageFeatureMapping.addAll(packagepProjectionMappings);

        // Map từng phần tử sang PackagesFeatureResponse và trả về Set
        return packageFeatureMapping.stream()
                .peek(item -> loopCounter.incrementAndGet()) // Theo dõi số vòng lặp
                .map(mapping -> {
                    loopCreateOppPackageFeature.incrementAndGet();
                    loopCreateOpp.incrementAndGet();

                    // Tạo đối tượng PackagesFeatureResponse
                    return PackagesFeatureResponse.builder()
                            .id(mapping.getPackageFeatureId())
                            .feature(mapping.getPackageFeatureName())
                            .packageId(mapping.getPackageId())
                            .build();
                })
                .collect(Collectors.toSet());
    }


    private Set<PackageImagesResponse> mapPackageImages(Set<PackagepProjectionMapping> packageFeatureMapping) {
        // Sử dụng TreeSet với Comparator để loại bỏ các phần tử trùng lặp theo packageImage
        Set<PackagepProjectionMapping> packageImagesMapping = new TreeSet<>(
                Comparator.comparing(mapping -> Optional.ofNullable(mapping.getPackageImage()).orElse(""))
        );

        // Thêm tất cả phần tử vào TreeSet
        packageImagesMapping.addAll(packageFeatureMapping);

        // Map từng phần tử sang PackageImagesResponse và trả về Set
        return packageImagesMapping.stream()
                .peek(item -> loopCounter.incrementAndGet()) // Theo dõi số vòng lặp
                .map(mapping -> {
                    loopCreateOppPackageFeature.incrementAndGet();
                    loopCreateOpp.incrementAndGet();

                    // Tạo đối tượng PackageImagesResponse
                    return PackageImagesResponse.builder()
                            .image(mapping.getPackageImage())
                            .build();
                })
                .collect(Collectors.toSet());
    }


    private PaymentTermResponse mapPaymentTerm(Set<PackagepProjectionMapping> packageProjectionMappings) throws SQLException {

        // Tạo TreeSet với Comparator để sắp xếp
        Set<PackagepProjectionMapping> packageTermProjectionMappings = new TreeSet<>(Comparator
                .comparingInt(PackagepProjectionMapping::getPaymentTermsId)
                .thenComparingInt(PackagepProjectionMapping::getPaymentConditionMonthlyId)
                .thenComparingInt(PackagepProjectionMapping::getPaymentConditionBudgeId));


        // Thêm tất cả các phần tử vào TreeSet
        packageTermProjectionMappings.addAll(packageProjectionMappings);

        // Kiểm tra nếu TreeSet rỗng
        if (packageTermProjectionMappings.isEmpty()) {
            throw new SQLException("No data found in packageProjectionMappings");
        }

        // Lấy phần tử đầu tiên
        PackagepProjectionMapping packageTerm = packageTermProjectionMappings.iterator().next();

        // Xây dựng đối tượng PaymentTermResponse
        return PaymentTermResponse.builder()
                .name(packageTerm.getPaymentTermsName())
                .price(packageTerm.getPaymentTermsPrice())
                .paymentConditionOnMonthlyResponses(mapPaymentConditionOnMonthly(packageTermProjectionMappings))
                .build();
    }


    private Set<PaymentConditionOnMonthlyResponse> mapPaymentConditionOnMonthly(Set<PackagepProjectionMapping> packageTermProjectionMappings) throws SQLException {
        Set<PackagepProjectionMapping> packagePaymentConditionOnMonthlyProjectionMapping = new TreeSet<>(new Comparator<PackagepProjectionMapping>() {
            @Override
            public int compare(PackagepProjectionMapping p1, PackagepProjectionMapping p2) {
                loopCounter.incrementAndGet();
                // So sánh dựa trên paymentConditionMonthlyId
                int compareMonthlyId = Integer.compare(p1.getPaymentConditionMonthlyId(), p2.getPaymentConditionMonthlyId());
                if (compareMonthlyId != 0) {
                    return compareMonthlyId;
                }

                // So sánh dựa trên paymentTermsId
                return Integer.compare(p1.getPaymentTermsId(), p2.getPaymentTermsId());
            }
        });

        // Thêm tất cả phần tử từ packageTermProjectionMappings vào packagePaymentConditionOnMonthlyProjectionMapping và sắp xếp chúng
        packagePaymentConditionOnMonthlyProjectionMapping.addAll(packageTermProjectionMappings);

        // Xử lý các phần tử đã sắp xếp
        return packagePaymentConditionOnMonthlyProjectionMapping.stream()
                .peek(item -> {
                    loopCounter.incrementAndGet();
                })
                .map(packagePaymentConditionOnMonthlyProjection -> {

                    loopCreateOppPaymentConditionOnMonthly.incrementAndGet();
                    loopCreateOpp.incrementAndGet();
                    // Chuyển đổi các phần tử thành PaymentConditionOnMonthlyResponse
                    return PaymentConditionOnMonthlyResponse.builder()
                            .id(packagePaymentConditionOnMonthlyProjection.getPaymentConditionMonthlyId())
                            .price(packagePaymentConditionOnMonthlyProjection.getPaymentConditionMonthlyPrice())
                            .durationMonths(packagePaymentConditionOnMonthlyProjection.getPaymentConditionMonthlyDurationMonths())
                            .build();
                }).collect(Collectors.toSet());
    }

    //    private Set<PaymentConditionOnMonthlyResponse> mapPaymentConditionOnMonthly(Set<PackagepProjectionMapping> packageTermProjectionMappings) throws SQLException {
//        return packageTermProjectionMappings.stream()
//                .peek(item -> {
//                    loopCounter.incrementAndGet();
//                })
//                .sorted(Comparator
//                        .comparingInt(PackagepProjectionMapping::getPaymentConditionMonthlyId)
//                        .thenComparingInt(PackagepProjectionMapping::getPaymentTermsId))
//                .map(packagePaymentConditionOnMonthlyProjection -> {
//                    loopCounter.incrementAndGet(); // Đếm trực tiếp trong map
//                    loopCreateOppPaymentConditionOnMonthly.incrementAndGet();
//                    loopCreateOpp.incrementAndGet();
//                    return PaymentConditionOnMonthlyResponse.builder()
//                            .price(packagePaymentConditionOnMonthlyProjection.getPaymentConditionMonthlyPrice())
//                            .durationMonths(packagePaymentConditionOnMonthlyProjection.getPaymentConditionMonthlyDurationMonths())
//                            .build();
//                })
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//    }
//
////
//    private Set<PaymentMethodResponse> mapPaymentMethods(Set<PackagepProjectionMapping> packageTermProjectionMappings) throws SQLException {
//        Set<PackagepProjectionMapping> packagePaymentMethodProjectionMapping = new TreeSet<>(new Comparator<PackagepProjectionMapping>() {
//            @Override
//            public int compare(PackagepProjectionMapping p1, PackagepProjectionMapping p2) {
//                loopCounter.incrementAndGet();
//                // So sánh dựa trên paymentConditionMonthlyId
//                int comparePaymentMethodId = Integer.compare(p1.getPaymentMethodId(), p2.getPaymentMethodId());
//                if (comparePaymentMethodId != 0) {
//                    return comparePaymentMethodId;
//                }
//
//
//                int compareBankGatewayId = Integer.compare(p1.getBankGatewayId(), p2.getBankGatewayId());
//                if (compareBankGatewayId != 0) {
//                    return compareBankGatewayId;
//                }
//
//                // So sánh dựa trên paymentTermsId
//                return Integer.compare(p1.getPaymentTermsId(), p2.getPaymentTermsId());
//            }
//        });
//
//        // Thêm tất cả phần tử từ packageTermProjectionMappings vào packagePaymentConditionOnMonthlyProjectionMapping và sắp xếp chúng
//        packagePaymentMethodProjectionMapping.addAll(packageTermProjectionMappings);
//
//
//        Set<PackagepProjectionMapping> PaymentMethodsMapping = new TreeSet<>(new Comparator<PackagepProjectionMapping>() {
//            @Override
//            public int compare(PackagepProjectionMapping p1, PackagepProjectionMapping p2) {
//                loopCounter.incrementAndGet();
//                // So sánh dựa trên paymentTermsId
//                return Integer.compare(p1.getPaymentMethodId(), p2.getPaymentMethodId());
//            }
//        });
//
//        // Thêm tất cả phần tử từ packageTermProjectionMappings vào packagePaymentConditionOnMonthlyProjectionMapping và sắp xếp chúng
//        PaymentMethodsMapping.addAll(packagePaymentMethodProjectionMapping);
//
//
//        return PaymentMethodsMapping.stream()
//                .peek(item -> {
//                    loopCounter.incrementAndGet();
//                }).map(paymentMethodMapping -> {
//
//                    loopCreateOppPaymentMethods.incrementAndGet();
//                    loopCreateOpp.incrementAndGet();
//                    // Chuyển đổi các phần tử thành PaymentConditionOnMonthlyResponse
//                    try {
//                        return PaymentMethodResponse.builder()
//                                .name(paymentMethodMapping.getPaymentMethodName())
//                                .description(paymentMethodMapping.getPaymentMethodDescription())
//                                .bankGatewayDetailRespons(mapBankGatewayResponses(paymentMethodMapping, packagePaymentMethodProjectionMapping))
//                                .build();
//                    } catch (SQLException e) {
//                        throw new RuntimeException(e);
//                    }
//                }).collect(Collectors.toSet());
//    }
//

//    private Set<PaymentMethodResponse> mapPaymentMethods(Set<PackagepProjectionMapping> packageTermProjectionMappings) throws SQLException {
//
//        // Sắp xếp và loại bỏ các phần tử trùng lặp dựa trên tiêu chí so sánh
//        Set<PackagepProjectionMapping> sortedMappings = packageTermProjectionMappings.stream()
//                .sorted(Comparator
//                        .comparingInt(PackagepProjectionMapping::getPaymentMethodId)
//                        .thenComparingInt(PackagepProjectionMapping::getBankGatewayId)
//                        .thenComparingInt(PackagepProjectionMapping::getPaymentTermsId))
//                .collect(Collectors.toCollection(LinkedHashSet::new));
//
//        // Ánh xạ sang PaymentMethodResponse
//        return sortedMappings.stream()
//                .peek(item -> loopCounter.incrementAndGet()) // Ghi nhận số lần lặp
//                .map(paymentMethodMapping -> {
//                    loopCreateOppPaymentMethods.incrementAndGet();
//                    loopCreateOpp.incrementAndGet();
//
//                    try {
//                        // Tạo PaymentMethodResponse từ PackagepProjectionMapping
//                        return PaymentMethodResponse.builder()
//                                .name(paymentMethodMapping.getPaymentMethodName())
//                                .description(paymentMethodMapping.getPaymentMethodDescription())
//                                .bankGatewayResponses(mapBankGatewayResponses(paymentMethodMapping, sortedMappings))
//                                .build();
//                    } catch (SQLException e) {
//                        throw new RuntimeException("Error mapping payment methods", e);
//                    }
//                })
//                .collect(Collectors.toSet());
//    }


//
//
//
//    private Set<BankGatewayDetailResponse> mapBankGatewayResponses(
//            PackagepProjectionMapping paymentMethodsMapping,
//            Set<PackagepProjectionMapping> packagePaymentMethodProjectionMapping
//    ) throws SQLException {
//
//        // Tạo TreeSet với comparator để sắp xếp
//        Set<PackagepProjectionMapping> packageBankGateWayMappings = new TreeSet<>(new Comparator<PackagepProjectionMapping>() {
//            @Override
//            public int compare(PackagepProjectionMapping p1, PackagepProjectionMapping p2) {
//                // So sánh dựa trên paymentMethodId
//                int comparePaymentMethodId = Integer.compare(p1.getPaymentMethodId(), p2.getPaymentMethodId());
//                if (comparePaymentMethodId != 0) {
//                    return comparePaymentMethodId;
//                }
//                // So sánh dựa trên bankGatewayId
//                return Integer.compare(p1.getBankGatewayId(), p2.getBankGatewayId());
//            }
//        });
//
//        // Thêm tất cả phần tử từ packagePaymentMethodProjectionMapping
//        packageBankGateWayMappings.addAll(packagePaymentMethodProjectionMapping);
//
//        // Chuyển đổi các phần tử thành BankGatewayResponse
//        return packageBankGateWayMappings.stream()
//                .peek(item -> {
//                    loopCounter.incrementAndGet();
//                })
//                .filter(mapping -> mapping.getPaymentMethodId() == paymentMethodsMapping.getPaymentMethodId())
//                .peek(item -> {
//                    loopCounter.incrementAndGet();
//                })// Lọc các phần tử phù hợp
//                .map(mapping -> {
//
//                    loopCreateOppBankGateway.incrementAndGet();
//                    loopCreateOpp.incrementAndGet();
//
//                    log.error("getBankGatewayCode:{}", mapping.getBankGatewayCode());
//                    if (mapping.getBankGatewayCode() == null) {
//                        log.error("nullpiiiiiii");
//                    }
//                    return new BankGatewayDetailResponse(
//                            null, // Giá trị null hoặc sửa lại nếu cần
//                            mapping.getBankGatewayName(),
//                            mapping.getBankGatewayUrl(),
//                            mapping.getBankGatewayCode());
//                }).collect(Collectors.toSet());
//    }
//

//    private List<PaymentConditionOnBudgetResponse> mapPaymentConditionOnBudget(ResultSet rs) throws SQLException {
//        List<PaymentConditionOnBudgetResponse> paymentConditionOnBudgetResponseList = new ArrayList<>();
//        Set<Integer> conditionIds = new HashSet<>();
//
//        do {
//
//
//                paymentConditionOnBudgetResponseList.add(PaymentConditionOnBudgetResponse.builder()
//                                        .maxBudget()
////                        .id(conditionId)
////                        .condition(rs.getString("budget_condition_name"))
//                        .build());
//                conditionIds.add(conditionId);
//
//        } while (rs.next());
//
//        return paymentConditionOnBudgetResponseList;
//    }


//    @Transactional
//    @Override
//    public PackagesResponse getPackage(Integer id) {
//
//        try {
//            Packages packages = findById(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//            log.error("packagesid:{}", packages.getId());
//            List<PackagesFeature> packagesFeatureList = packagesFeatureRepository.getAllByPackageId(packages.getId()).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
//             PaymentTermResponse paymentTermResponse=  paymentTermService.getPaymentTerm(packages,null );
//            return PackagesResponse.toPackagesResponse(packages,packagesFeatureList,paymentTermResponse);
//        } catch (Exception e) {
//            throw e;
//        }
//    }


    @Transactional
    @Override
    public PackagesResponse getPackage(Integer id) {
        try {
            return getPackageById(id);
        } catch (Exception e) {
            throw e;
        }
    }


    @Transactional
    @Override
    public Set<PackagesResponse> getAllPackage(Integer categorieId) throws SQLException {
        try {
            return getAllPackageByCategorieId(categorieId);
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public void deletePackage(Integer id) {
        try {
            Packages packages = findById(id).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            log.error("packagesid:{}", packages.getId());
            PaymentTerms paymentTerms = paymentTermRepository.findByPackagesId(packages.getId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            paymentTermService.deletePaymentTerm(paymentTerms.getId());
            deleteById(packages.getId());
            deletionUtil.deleteByParentId(packages.getId(), "package_id");
        } catch (Exception e) {
            throw e;
        }
    }


}
