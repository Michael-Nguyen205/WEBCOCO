package spring.boot.webcococo.models.mapping;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Data // Lombok annotation to generate getters, setters, toString, etc.
public class PackagepProjectionMapping {

    static AtomicInteger loopCreatePackagepProjectionMapping = new AtomicInteger(0);
    // Package fields
    private Integer packageId;
    private String packageName;
    private String packageDescription;
    private Integer packageCategoriesId;
    private String packageImage;
    private Integer packageDepositPercent;
    // Package Feature fields
    private Integer packageFeatureId;
    private String packageFeatureName;
    // Payment Terms fields
    private Integer paymentTermsId;
    private String paymentTermsName;
    private BigDecimal paymentTermsPrice;
    // Payment Condition Budget fields
    private Integer paymentConditionBudgeId;
    private BigDecimal paymentConditionBudgetMin;
    private BigDecimal paymentConditionBudgetMax;
    private BigDecimal paymentConditionBudgetFixedFee;
    private BigDecimal paymentConditionBudgetPercentageFee;
    // Payment Condition Monthly fields
    private Integer paymentConditionMonthlyId;
    private BigDecimal paymentConditionMonthlyPrice;
    private Integer paymentConditionMonthlyDurationMonths;

    public static Set<PackagepProjectionMapping> toPackageMapping(ResultSet rs, AtomicInteger loopCounter, AtomicInteger loopCreateOpp) throws SQLException {

        Set<PackagepProjectionMapping> packagepProjectionMappings = new HashSet<>();
        long startTime = System.currentTimeMillis();

        while (rs.next()) {
            loopCreatePackagepProjectionMapping.incrementAndGet();
            loopCounter.incrementAndGet();
            loopCreateOpp.incrementAndGet();

            PackagepProjectionMapping packagepProjectionMapping = new PackagepProjectionMapping();

            // Mapping package fields
            packagepProjectionMapping.setPackageId(rs.getInt("p_id"));
            packagepProjectionMapping.setPackageName(rs.getString("p_name"));
            packagepProjectionMapping.setPackageDescription(rs.getString("p_description"));
            packagepProjectionMapping.setPackageCategoriesId(rs.getInt("p_categories_id"));
            packagepProjectionMapping.setPackageImage(rs.getString("p_image"));
            packagepProjectionMapping.setPackageDepositPercent(rs.getInt("p_deposit_percent"));

            // Mapping package feature fields
            packagepProjectionMapping.setPackageFeatureId(rs.getInt("pf_id"));
            packagepProjectionMapping.setPackageFeatureName(rs.getString("pf_name"));

            // Mapping payment terms fields
            packagepProjectionMapping.setPaymentTermsId(rs.getInt("pt_id"));
            packagepProjectionMapping.setPaymentTermsName(rs.getString("pt_name"));
            packagepProjectionMapping.setPaymentTermsPrice(rs.getBigDecimal("pt_price"));

            // Mapping payment condition on budget fields
            packagepProjectionMapping.setPaymentConditionBudgeId(rs.getInt("pcb_id"));
            packagepProjectionMapping.setPaymentConditionBudgetMin(rs.getBigDecimal("pcb_min_budget"));
            packagepProjectionMapping.setPaymentConditionBudgetMax(rs.getBigDecimal("pcb_max_budget"));
            packagepProjectionMapping.setPaymentConditionBudgetFixedFee(rs.getBigDecimal("pcb_fixed_fee"));
            packagepProjectionMapping.setPaymentConditionBudgetPercentageFee(rs.getBigDecimal("pcb_percentage_fee"));

            // Mapping payment condition on monthly fields
            packagepProjectionMapping.setPaymentConditionMonthlyId(rs.getInt("pcm_id"));
            packagepProjectionMapping.setPaymentConditionMonthlyPrice(rs.getBigDecimal("pcm_price"));
            packagepProjectionMapping.setPaymentConditionMonthlyDurationMonths(rs.getInt("pcm_duration_months"));




            // Add the mapped object to the set
            packagepProjectionMappings.add(packagepProjectionMapping);
        }


        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.error("Time mapping record sql to model mapping: " + duration + " milliseconds");
        log.error("loopCreatePackagepProjectionMapping:{}", loopCreatePackagepProjectionMapping);

        return packagepProjectionMappings;
    }


}
