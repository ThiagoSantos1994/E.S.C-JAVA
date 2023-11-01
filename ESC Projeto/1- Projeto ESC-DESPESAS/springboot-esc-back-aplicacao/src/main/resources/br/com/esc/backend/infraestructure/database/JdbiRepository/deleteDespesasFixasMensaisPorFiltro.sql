DELETE FROM
    tbd_DespesasFixasMensais
WHERE
   id_Despesa = :idDespesa
   AND id_Ordem = :idOrdem
   AND id_Funcionario = :idFuncionario