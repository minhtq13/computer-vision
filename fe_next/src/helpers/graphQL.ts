/**
 *
 * @returns
 * @param queryName tên query
 * @param contentQuery nội dung query dữ liệu
 * @param paging có phân trang hay không
 */
export const buildGraphQLQuery = (queryName: any, contentQuery: any, paging = false) => {
  return `query {
        ${queryName} {
            ${contentQuery} \n
            ${paging ? queryPageGraphQL : ""}
        }
    } `;
};

export const queryPageGraphQL =
  "pageable { \n" +
  "        offset, pageNumber, pageSize, \n" +
  "        sort { \n" +
  "            empty, sorted \n" +
  "        }, paged, unpaged \n" +
  "}, \n" +
  "empty, first, last, number, numberOfElements, totalPages, totalElements \n";
