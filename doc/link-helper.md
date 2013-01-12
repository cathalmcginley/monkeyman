---
tags: monkeyman, helper, link, hyperlink, non-blog
pubDateTime: 2013-01-12
---
#Link helper

`HyperLinkHelper` is a very simple hepler, available as `Link` in your
SCAML or JADE layouts. It should simplify blocks of text with several
links, rather than having to nest `a` elements and their text content
(with the trouble of spaces before trailing punctuation):

    p
      | Read more
      a(href="about.html") about
      | this item
      a(href="later.html")
        | later
      |.

Instead we simply use Link:

    p
      | Read more
        != Link.to("about.html", "about")
      | this item
        != Link.to("later.html", "later", ".")

Note that as `Link.to` returns HTML, you must disable escaping with
`!=` rather than simply `=`.