---
tags: monkeyman, helper, date, non-blog
pubDateTime: 2013-01-12
---
#Date span helper

`DateSpanHelper`, available as `Date` in your SCAML or JADE layouts,
is for formatting dates within a `span` element.  It uses (English)
month names and ordinal suffixes (the latter superscripted) and 
includes an ISO-formatted date as the span title. For example, this:

    div.last-modified
      != Date.span(2013, 01, 12)

Produces this:

    <div class="last-modified">
      <span class="date" title="2013-01-12">12<sup>th</sup> January 2013</span>
    </div>
