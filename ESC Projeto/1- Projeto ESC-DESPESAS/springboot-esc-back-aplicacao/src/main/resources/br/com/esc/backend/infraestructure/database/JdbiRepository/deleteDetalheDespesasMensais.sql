DELETE FROM
    tbd_DetalheDespesasMensais
WHERE
   id_Despesa = :idDespesa
   AND id_DetalheDespesa = :idDetalheDespesa
   AND id_Funcionario = :idFuncionario