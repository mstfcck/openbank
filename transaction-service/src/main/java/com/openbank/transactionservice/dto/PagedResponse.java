package com.openbank.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic paginated response wrapper.
 *
 * @param <T> The type of content in the page
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Schema(description = "Paginated response wrapper")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    @Schema(description = "List of items for the current page")
    @JsonProperty("content")
    private List<T> content;

    @Schema(description = "Current page number (0-based)",
           example = "0")
    @JsonProperty("page")
    private int page;

    @Schema(description = "Number of items per page",
           example = "20")
    @JsonProperty("size")
    private int size;

    @Schema(description = "Total number of elements across all pages",
           example = "100")
    @JsonProperty("totalElements")
    private long totalElements;

    @Schema(description = "Total number of pages",
           example = "5")
    @JsonProperty("totalPages")
    private int totalPages;

    @Schema(description = "Whether this is the first page",
           example = "true")
    @JsonProperty("first")
    private boolean first;

    @Schema(description = "Whether this is the last page",
           example = "false")
    @JsonProperty("last")
    private boolean last;

    @Schema(description = "Whether there is a next page",
           example = "true")
    @JsonProperty("hasNext")
    private boolean hasNext;

    @Schema(description = "Whether there is a previous page",
           example = "false")
    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    /**
     * Create a PagedResponse from a Spring Data Page object
     */
    public static <T> PagedResponse<T> of(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
