//package spring.boot.authenauthor.repositories;
//
//import org.springframework.data.domain.Page;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import spring.boot.webcococo.models.pojos.CommonCondition;
//
//import java.util.List;
//
//public interface CommonNativeRepository {
//
//     <T> Page<T> queryForList(String query, String countQuery, Object[] params, RowMapper<T> rowMapper,
//                              CommonCondition paginationRequest, int[] argTypes);
//    <T> T queryForObject(JdbcTemplate jdbcTemplate, String query, Object[] params, RowMapper<T> rowMapper, int[] argTypes);
//    <T> List<T> queryForListWithInClause(JdbcTemplate jdbcTemplate, String query, List<Object> params, RowMapper<T> rowMapper, int[] argTypes);
//
//}
