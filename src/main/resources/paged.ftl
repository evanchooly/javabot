<table class="zebra-striped">
    <tr>
        <td class="right top" colspan="4">
            <div id="pagination" class="pagination">
                <ul>
                    <li id="previousPage" class="prev <#if !previousPage?? >disabled</#if>">
                        <a <#if previousPage?? >href="${previousPage}"</#if>>&larr; Previous</a>
                    </li>

                    <li id="currentPage" class="current"> Displaying ${startRange} to ${endRange} of ${itemCount} </li>

                    <li id="nextPage" class="next <#if !nextPage?? >disabled</#if>">
                        <a <#if nextPage?? >href="${nextPage}"</#if>>Next &rarr;</a>
                    </li>
                </ul>
            </div>
        </td>
    </tr>
<#include getPagedView() >
</table>
