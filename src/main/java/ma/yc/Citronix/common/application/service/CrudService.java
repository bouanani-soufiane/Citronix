package ma.yc.Citronix.common.application.service;

import org.springframework.data.domain.Page;

public interface CrudService<ID, CreateRequestDTO, UpdateRequestDTO, ResponseDTO> {
    Page<ResponseDTO> findAll ( int pageNum, int pageSize );

    ResponseDTO findById ( ID id );

    ResponseDTO create ( CreateRequestDTO dto );

    ResponseDTO update ( ID id, UpdateRequestDTO dto );

    void delete ( ID id );
}