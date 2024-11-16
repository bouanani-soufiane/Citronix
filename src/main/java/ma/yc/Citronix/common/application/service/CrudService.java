package ma.yc.Citronix.common.application.service;

import org.springframework.data.domain.Page;

public interface CrudService<ID, RequestDTO, ResponseDTO> {
    Page<ResponseDTO> findAll ( int pageNum, int pageSize );

    ResponseDTO findById ( ID id );

    ResponseDTO create ( RequestDTO dto );

    ResponseDTO update ( ID id, RequestDTO dto );

    void delete ( ID id );
}