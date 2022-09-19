sed -E "s/\"label\": \"[^}]*/&,_&/g" nomenclature/PROF2022H.json | sed -E "s/_\"label\"/\"label2\"/g"
