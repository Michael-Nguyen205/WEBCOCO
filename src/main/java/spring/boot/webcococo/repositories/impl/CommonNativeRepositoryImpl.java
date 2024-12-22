package spring.boot.webcococo.repositories.impl;//package spring.boot.authenauthor.repositories.impl;
//
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Component;
//import spring.boot.authenauthor.repositories.CommonNativeRepository;
//
//import java.util.List;
//
//
//public class CommonNativeRepositoryImpl implements CommonNativeRepository {
//
//    private  JdbcTemplate jdbcTemplate;
//
//    public CommonNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public <T> Page<T> queryForList(String query, String countQuery, Object[] params, RowMapper<T> rowMapper, CommonCondition paginationRequest, int[] argTypes) {
//        int page = paginationRequest.getPage();
//        int size = paginationRequest.getSize();
//        int offset = (page - 1) * size;
//
//        Long totalRecords = jdbcTemplate.queryForObject(countQuery, Long.class, params);
//
//        List<T> data = jdbcTemplate.query(query + " LIMIT ? OFFSET ?", paramsWithLimitAndOffset(params, size, offset), argTypes, rowMapper);
//
//        Pageable pageable = PageRequest.of(page - 1, size);
//
//        return new PageImpl<>(data, pageable, totalRecords);
//
//    }
//
//    @Override
//    public <T> T queryForObject(JdbcTemplate jdbcTemplate, String query, Object[] params, RowMapper<T> rowMapper, int[] argTypes) {
//        return jdbcTemplate.queryForObject(query, params, argTypes, rowMapper);
//    }
//
//    @Override
//    public <T> List<T> queryForListWithInClause(JdbcTemplate jdbcTemplate, String query, List<Object> params, RowMapper<T> rowMapper, int[] argTypes) {
//        return jdbcTemplate.query(query, params.toArray(), argTypes, rowMapper);
//    }
//
//    private static Object[] paramsWithLimitAndOffset(Object[] params, int limit, int offset) {
//        Object[] result = new Object[params.length + 2];
//        System.arraycopy(params, 0, result, 0, params.length);
//        result[params.length] = limit;
//        result[params.length + 1] = offset;
//        return result;
//    }
//}
//
