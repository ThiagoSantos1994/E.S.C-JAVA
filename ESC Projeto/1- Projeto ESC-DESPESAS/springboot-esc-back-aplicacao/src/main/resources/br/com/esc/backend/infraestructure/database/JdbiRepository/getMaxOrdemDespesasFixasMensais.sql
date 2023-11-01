SELECT
    MAX(id_Ordem) + 1
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario =:idFuncionario