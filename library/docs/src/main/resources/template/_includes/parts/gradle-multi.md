{% if project["modules"] is defined and project["modules"] | length > 1 %}

=== "Dependencies"

    Simply add the dependencies inside your `build.gradle.kts` file.

    ```kotlin title="build.gradle.kts"

    val {{ project["library"]["id"] }} = "<LATEST-VERSION>"
    {% for group in project["groups"] %}
    // {{ group["gradle-comment" ]}}
    {% for module in project["modules"] -%}
    {% if module["group"] == group["id"] -%}
    implementation("{{ project["library"]["maven"] }}:{{ module["id"] }}:${{ project["library"]["id"] }}")
    {% endif -%}
    {%- endfor -%}
    {%- endfor -%}
    ```

=== "Version Catalog"

    Define the dependencies inside your `libs.versions.toml` file.

    ```toml title="libs.versions.toml"
    [versions]

    {{ project["library"]["id"] }} = "<LATEST-VERSION>"
    
    {%- set l1 = project["library"]["id"] | length -%}
    {%- set ns = namespace(maxLength=0) -%}
    {%- for group in project["groups"] -%}
      {%- for module in group["modules"] -%}   
        {%- set l2 = module | length -%}
        {%- if l2 > ns.maxLength -%}
          {%- set ns.maxLength = l2 -%}
        {%- endif -%}
      {%- endfor -%}
    {%- endfor -%}

    {%- set padding = ns.maxLength + l1 + 3 -%}
    {%- set padding2 = ns.maxLength + 4 + project["library"]["maven"] | length %}

    [libraries]
    {% for group in project["groups"] %}
    # {{ group["gradle-comment" ]}}
    {% for module in project["modules"] -%}
    {% if module["group"] == group["id"] -%}
      {%- set name = project["library"]["id"] ~ "-" ~ module["id"] ~ " =" -%}
      {%- set module2 = "\"" ~ project["library"]["maven"] ~ ":" ~ module["id"] ~ "\"," -%}
    {{ name.ljust(padding) }} { module = {{ module2.ljust(padding2) }} version.ref = "{{ project["library"]["id"] }}" }
    {% endif -%}
    {% endfor %}
    {%- endfor -%}
    ```

    And then use the definitions in your projects like following:

    ```gradle title="build.gradle.kts"
    {% for group in project["groups"] %}
    # {{ group["gradle-comment" ]}}
    {% for module in project["modules"] -%}
    {% if module["group"] == group["id"] -%}
    implementation(libs.{{ project["library"]["id"] }}.{{ module["id"] | replace("-", ".") }})
    {% endif -%}
    {% endfor %}
    {%- endfor -%}
    
    ```

{% endif %}