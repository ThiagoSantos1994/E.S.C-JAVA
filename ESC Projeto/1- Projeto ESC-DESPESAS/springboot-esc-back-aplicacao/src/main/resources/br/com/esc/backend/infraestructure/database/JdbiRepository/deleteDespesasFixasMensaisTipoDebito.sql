DELETE FROM
    tbd_DespesasFixasMensais
WHERE
   id_Despesa = :idDespesa
   AND id_DetalheDespesaDebitoCartao = :idDetalheDespesaDebitoCartao
   AND id_Funcionario = :idFuncionario