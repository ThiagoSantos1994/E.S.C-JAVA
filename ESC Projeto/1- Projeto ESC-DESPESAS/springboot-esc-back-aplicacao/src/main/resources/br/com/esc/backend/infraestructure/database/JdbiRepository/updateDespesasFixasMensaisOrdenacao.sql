UPDATE
   tbd_DespesasFixasMensais
SET
   id_Ordem = :idNovaOrdem
WHERE
   id_Despesa = :idDespesa
   AND id_Ordem = :idOrdem
   AND id_Funcionario = :idFuncionario