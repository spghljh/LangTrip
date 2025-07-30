package kr.co.sist.e_learning.admin;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO_donation<EN>
{
    private List<EN> list;
    private int totalCnt;
    private PageRequestDTO_donation pageRequestDTO;

    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean hasPrev;
    private boolean next;
    private boolean hasNext;

    private int page;
    private int pageSize;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO_donation(List<EN> list, int totalCnt, PageRequestDTO_donation pageRequestDTO) {
        this.list = list;
        this.totalCnt = totalCnt;
        this.pageRequestDTO = pageRequestDTO;
        this.page = pageRequestDTO.getPage();
        this.pageSize = pageRequestDTO.getSize();

        int end = (int)(Math.ceil(this.page / 10.0)) * 10;
        this.startPage = end - 9;
        int lastPage = (int)(Math.ceil(totalCnt / (double)pageSize));
        this.endPage = Math.min(end, lastPage);

        this.prev = this.startPage > 1;
        this.hasPrev = this.startPage > 1;
        this.next = this.endPage < lastPage;
        this.hasNext = this.endPage < lastPage;
    }

    public <DTO> PageResponseDTO_donation<DTO> convertToDTO(Function<EN, DTO> fn) {
        List<DTO> dtoList = list.stream().map(fn).collect(Collectors.toList());
        return PageResponseDTO_donation.<DTO>withAll()
                .list(dtoList)
                .totalCnt(totalCnt)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    public int getStartRow() {
        return (page - 1) * pageSize + 1;
    }
}
